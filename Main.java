package filas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.Scanner;

class PilhaGrupo {
    private Stack<String> grupo = new Stack<>();

    public void adicionarPessoa(String pessoa) {
        grupo.push(pessoa);
    }

    public String verificarConhecimento(String pessoa1, String pessoa2) {
        if (grupo.contains(pessoa1) && grupo.contains(pessoa2)) {
            return pessoa1 + " conhece " + pessoa2;
        }
        return pessoa1 + " não conhece " + pessoa2;
    }

    public boolean pessoaExiste(String pessoa) {
        return grupo.contains(pessoa);
    }

    public String listarPessoas() {
        StringBuilder sb = new StringBuilder();
        for (String pessoa : grupo) {
            sb.append(pessoa).append(",");
        }
        // Remova a última vírgula
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        // Especifique o caminho absoluto para o arquivo 'grupos.txt'
        String caminhoDoArquivo = "C:\\Users\\Victo\\OneDrive\\Documentos\\NetBeansProjects\\FILAS\\src\\filas\\grupos.txt";

        PilhaGrupo[] grupos = carregarGruposDoArquivo(caminhoDoArquivo);

        if (grupos == null) {
            System.out.println("Erro ao carregar grupos. Verifique o arquivo 'grupos.txt'.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            int grupoConsulta = -1;

            while (grupoConsulta < 0 || grupoConsulta > 5) {
                try {
                    System.out.println("Digite o número do grupo (1-5) ou digite 0 para mostrar todos os grupos:");
                    grupoConsulta = scanner.nextInt();
                    if (grupoConsulta < 0 || grupoConsulta > 5) {
                        System.out.println("Grupo inválido. Digite um número de grupo entre 0 e 5.");
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Entrada inválida. Digite um número de grupo válido.");
                    scanner.next(); // Limpa a entrada incorreta
                }
            }

            if (grupoConsulta == 0) {
                // Mostrar todos os grupos
                mostrarGrupos(grupos);
            } else {
                PilhaGrupo grupo = grupos[grupoConsulta - 1];

                System.out.println("Digite o nome da primeira pessoa:");
                String pessoa1 = scanner.next().toLowerCase();
                System.out.println("Digite o nome da segunda pessoa:");
                String pessoa2 = scanner.next().toLowerCase();

                System.out.println("Digite o nome da pessoa a buscar:");
                String pessoaABuscar = scanner.next().toLowerCase();

                String resultado1 = grupo.verificarConhecimento(pessoa1, pessoa2);
                String resultado2 = "";

                for (PilhaGrupo grupoExistente : grupos) {
                    if (grupoExistente.pessoaExiste(pessoaABuscar)) {
                        if (grupoExistente.equals(grupo)) {
                            resultado2 = pessoaABuscar + " está no grupo " + (grupos[grupoConsulta - 1] == grupoExistente ? grupoConsulta : obterNumeroGrupo(grupos, grupoExistente)) + ".";
                        } else {
                            resultado2 = pessoaABuscar + " não se encontra nesse grupo, ela pertence ao grupo " + obterNumeroGrupo(grupos, grupoExistente) + ".";
                        }
                        break;
                    }
                }
                if (resultado2.isEmpty()) {
                    resultado2 = pessoaABuscar + " não existe em nenhum grupo.";
                }

                System.out.println("\nResultado:");
                System.out.println(resultado1);
                System.out.println(resultado2);
            }

            System.out.println("\nDeseja pesquisar outro grupo? (Sim ou Não):");
            String resposta = scanner.next().toLowerCase();

            if (!resposta.equals("sim")) {
                break;
            }
        }

        scanner.close();
    }

    public static PilhaGrupo[] carregarGruposDoArquivo(String caminhoDoArquivo) {
        PilhaGrupo[] grupos = new PilhaGrupo[5];
        try {
            File arquivo = new File(caminhoDoArquivo);
            if (!arquivo.exists()) {
                System.out.println("O arquivo 'grupos.txt' não foi encontrado.");
                return null;
            }

            FileReader fileReader = new FileReader(arquivo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for (int i = 0; i < 5; i++) {
                String linha = bufferedReader.readLine();
                if (linha == null) {
                    System.out.println("Erro de formato no arquivo 'grupos.txt'. Verifique se há grupos suficientes.");
                    return null;
                }
                String[] elementos = linha.split(",");
                PilhaGrupo grupo = new PilhaGrupo();
                for (String pessoa : elementos) {
                    grupo.adicionarPessoa(pessoa.trim().toLowerCase());
                }
                grupos[i] = grupo;
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grupos;
    }

    public static void mostrarGrupos(PilhaGrupo[] grupos) {
        for (int i = 0; i < grupos.length; i++) {
            String listaPessoas = grupos[i].listarPessoas();
            System.out.println("Grupo " + obterNumeroGrupo(grupos, grupos[i]) + " : " + listaPessoas);
        }
    }

    public static int obterNumeroGrupo(PilhaGrupo[] grupos, PilhaGrupo grupo) {
        for (int i = 0; i < grupos.length; i++) {
            if (grupos[i].equals(grupo)) {
                return i + 1;
            }
        }
        return -1;
    }
}


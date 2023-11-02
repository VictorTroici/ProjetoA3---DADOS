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
        return pessoa1 + " nao conhece " + pessoa2;
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
                    System.out.println("Digite o numero do grupo (1-5) ou digite 0 para mostrar todos os grupos:");
                    grupoConsulta = scanner.nextInt();
                    if (grupoConsulta < 0 || grupoConsulta > 5) {
                        System.out.println("Grupo invalido. Digite um numero de grupo entre 0 e 5.");
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Entrada invalida. Digite um numero de grupo valido.");
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

                String resultado = grupo.verificarConhecimento(pessoa1, pessoa2);
                System.out.println(resultado);

                System.out.println("Digite o nome da pessoa a buscar:");
                String pessoaABuscar = scanner.next().toLowerCase();

                if (grupo.pessoaExiste(pessoaABuscar)) {
                    System.out.println(pessoaABuscar + " existe no grupo " + grupoConsulta + ".");
                } else {
                    System.out.println(pessoaABuscar + " nao existe no grupo " + grupoConsulta + ".");
                }
            }

            System.out.println("Deseja pesquisar outro grupo? (Sim ou Nao):");
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
                System.out.println("O arquivo 'grupos.txt' nao foi encontrado.");
                return null;
            }

            FileReader fileReader = new FileReader(arquivo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for (int i = 0; i < 5; i++) {
                String linha = bufferedReader.readLine();
                if (linha == null) {
                    System.out.println("Erro de formato no arquivo 'grupos.txt'. Verifique se ha grupos suficientes.");
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
            System.out.println("Grupo " + (i + 1) + " : " + listaPessoas);
        }
    }
}





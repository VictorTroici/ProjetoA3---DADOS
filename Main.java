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

        // Mostrar todos os grupos
        mostrarGrupos(grupos);

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDigite o primeiro nome que deseja procurar:");
        String pessoa1 = scanner.next().toLowerCase();

        System.out.println("Digite o segundo nome que deseja procurar:");
        String pessoa2 = scanner.next().toLowerCase();

        System.out.println("Digite o nome da primeira pessoa que deseja saber se existe:");
        String pessoaABuscar1 = scanner.next().toLowerCase();

        System.out.println("Digite o nome da segunda pessoa que deseja saber se existe:");
        String pessoaABuscar2 = scanner.next().toLowerCase();

        // Verificar conhecimento entre as duas primeiras pessoas
        String resultado1 = verificarConhecimentoEntrePessoas(grupos, pessoa1, pessoa2);
        System.out.println("\nResultado:");
        System.out.print(resultado1);

        // Verificar se a primeira pessoa existe em algum grupo
        String resultado2 = verificarExistenciaPessoa(grupos, pessoaABuscar1);
        System.out.println(resultado2);

        // Verificar se a segunda pessoa existe em algum grupo
        String resultado3 = verificarExistenciaPessoa(grupos, pessoaABuscar2);
        System.out.println(resultado3);

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
            System.out.println("Grupo " + (i + 1) + " : " + listaPessoas);
        }
    }

    public static String verificarConhecimentoEntrePessoas(PilhaGrupo[] grupos, String pessoa1, String pessoa2) {
        StringBuilder resultado = new StringBuilder();
        boolean conhecem = false;

        for (PilhaGrupo grupo : grupos) {
            String validacao = grupo.verificarConhecimento(pessoa1, pessoa2);
            resultado.append(validacao).append("\n");

            // Se já encontrou uma vez que se conhecem, não precisa continuar verificando
            if (validacao.contains("conhece")) {
                conhecem = true;
                break;
            }
        }

        // Se não se conhecem em nenhum grupo, exiba uma mensagem única
        if (!conhecem) {
            resultado.setLength(0);
            resultado.append(pessoa1).append(" nao conhece ").append(pessoa2);
        }

        return resultado.toString();
    }

    public static String verificarExistenciaPessoa(PilhaGrupo[] grupos, String pessoaABuscar) {
        for (PilhaGrupo grupo : grupos) {
            if (grupo.pessoaExiste(pessoaABuscar)) {
                return pessoaABuscar + " existe.";
            }
        }
        return pessoaABuscar + " nao existe.";
    }
}

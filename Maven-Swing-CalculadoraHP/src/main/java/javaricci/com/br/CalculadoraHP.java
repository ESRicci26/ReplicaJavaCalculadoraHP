package javaricci.com.br;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CalculadoraHP extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField campoTextoVisor;
    private final Map<String, Double> registrosFinanceiros;
    private final DecimalFormat formatoDecimal;
    
    // Registros financeiros
    private static final String VP = "VP"; // Valor Presente
    private static final String VF = "VF"; // Valor Futuro  
    private static final String PMT = "PMT"; // Pagamento
    private static final String TAXA = "i"; // Taxa de juros ao mês
    private static final String PERIODO = "n"; // Número de períodos
    
    private String ultimaOperacao = "";
    private boolean aguardandoNovoValor = false;
    private double valorAnterior = 0;

    public CalculadoraHP() {
        registrosFinanceiros = new HashMap<>();
        formatoDecimal = new DecimalFormat("#,##0.00");
        campoTextoVisor = new JTextField("0");
        inicializarRegistros();
        inicializarComponentes();
    }
    
    private void inicializarRegistros() {
        registrosFinanceiros.put(VP, 0.0);
        registrosFinanceiros.put(VF, 0.0);
        registrosFinanceiros.put(PMT, 0.0);
        registrosFinanceiros.put(TAXA, 0.0);
        registrosFinanceiros.put(PERIODO, 0.0);
    }

    private void inicializarComponentes() {
        campoTextoVisor.setFont(new Font("Consolas", Font.BOLD, 24));
        campoTextoVisor.setHorizontalAlignment(JTextField.RIGHT);
        //campoTextoVisor.setDocument(new PermitiNumerosOperadoresMatematicos());
        campoTextoVisor.setPreferredSize(new Dimension(350, 50));
        campoTextoVisor.setEditable(true);
        
        // Adiciona listener para ENTER
        campoTextoVisor.addActionListener(e -> confirmarEntrada());

        // Configuração da janela
        setTitle("Calculadora HP Financeira");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // Layout principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Painel do visor
        JPanel painelVisor = new JPanel(new BorderLayout());
        painelVisor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelVisor.add(campoTextoVisor, BorderLayout.CENTER);
        
        // Painel dos botões
        JPanel painelBotoes = new JPanel(new GridLayout(6, 4, 5, 5));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Primeira linha: Funções financeiras
        painelBotoes.add(criarBotaoFinanceiro("VP", Color.BLUE));
        painelBotoes.add(criarBotaoFinanceiro("VF", Color.BLUE));
        painelBotoes.add(criarBotaoFinanceiro("PMT", Color.BLUE));
        painelBotoes.add(criarBotao("C", e -> limparTudo(), Color.RED));
        
        // Segunda linha: Mais funções financeiras
        painelBotoes.add(criarBotaoFinanceiro("i", Color.BLUE));
        painelBotoes.add(criarBotaoFinanceiro("n", Color.BLUE));
        painelBotoes.add(criarBotao("CHS", e -> mudarSinal(), Color.ORANGE));
        painelBotoes.add(criarBotao("÷", e -> operacao("/"), Color.GRAY));
        
        // Terceira linha: 7, 8, 9, *
        painelBotoes.add(criarBotaoNumerico("7"));
        painelBotoes.add(criarBotaoNumerico("8"));
        painelBotoes.add(criarBotaoNumerico("9"));
        painelBotoes.add(criarBotao("×", e -> operacao("*"), Color.GRAY));
        
        // Quarta linha: 4, 5, 6, -
        painelBotoes.add(criarBotaoNumerico("4"));
        painelBotoes.add(criarBotaoNumerico("5"));
        painelBotoes.add(criarBotaoNumerico("6"));
        painelBotoes.add(criarBotao("-", e -> operacao("-"), Color.GRAY));
        
        // Quinta linha: 1, 2, 3, +
        painelBotoes.add(criarBotaoNumerico("1"));
        painelBotoes.add(criarBotaoNumerico("2"));
        painelBotoes.add(criarBotaoNumerico("3"));
        painelBotoes.add(criarBotao("+", e -> operacao("+"), Color.GRAY));
        
        // Sexta linha: 0, ., =, ENTER
        painelBotoes.add(criarBotaoNumerico("0"));
        painelBotoes.add(criarBotao(".", e -> adicionarPonto()));
        painelBotoes.add(criarBotao("=", e -> calcularResultado(), Color.GREEN));
        painelBotoes.add(criarBotao("ENTER", e -> confirmarEntrada(), Color.GREEN));

        painelPrincipal.add(painelVisor, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        
        // Painel de informações dos registros
        JPanel painelInfo = criarPainelInformacoes();
        painelPrincipal.add(painelInfo, BorderLayout.SOUTH);
        
        add(painelPrincipal);
        pack();
        setLocationRelativeTo(null);
    }
    
    private JPanel criarPainelInformacoes() {
        JPanel painel = new JPanel(new GridLayout(1, 5, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Registros Financeiros"));
        painel.setPreferredSize(new Dimension(350, 60));
        
        for (String registro : new String[]{VP, VF, PMT, TAXA, PERIODO}) {
            JLabel label = new JLabel(registro + ": 0.00", SwingConstants.CENTER);
            label.setFont(new Font("Consolas", Font.PLAIN, 10));
            label.setBorder(BorderFactory.createEtchedBorder());
            painel.add(label);
        }
        
        return painel;
    }

    private JButton criarBotaoNumerico(String numero) {
        return criarBotao(numero, e -> digitarNumero(numero));
    }
    
    private JButton criarBotaoFinanceiro(String funcao, Color cor) {
        JButton botao = criarBotao(funcao, e -> processarFuncaoFinanceira(funcao), cor);
        botao.setToolTipText("Clique para calcular " + funcao + " ou armazenar valor");
        return botao;
    }

    private JButton criarBotao(String texto, ActionListener acao) {
        return criarBotao(texto, acao, Color.LIGHT_GRAY);
    }
    
    private JButton criarBotao(String texto, ActionListener acao, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Consolas", Font.BOLD, 14));
        botao.setPreferredSize(new Dimension(70, 40));
        botao.addActionListener(acao);
        botao.setFocusPainted(false);
        botao.setBackground(cor);
        return botao;
    }

    private void digitarNumero(String numero) {
        if (aguardandoNovoValor) {
            campoTextoVisor.setText(numero);
            aguardandoNovoValor = false;
        } else {
            String textoAtual = campoTextoVisor.getText();
            if (textoAtual.equals("0")) {
                campoTextoVisor.setText(numero);
            } else {
                campoTextoVisor.setText(textoAtual + numero);
            }
        }
    }
    
    private void adicionarPonto() {
        String texto = campoTextoVisor.getText();
        if (aguardandoNovoValor) {
            campoTextoVisor.setText("0.");
            aguardandoNovoValor = false;
        } else if (!texto.contains(".")) {
            campoTextoVisor.setText(texto + ".");
        }
    }
    
    private void mudarSinal() {
        try {
            double valor = Double.parseDouble(campoTextoVisor.getText());
            campoTextoVisor.setText(String.valueOf(-valor));
        } catch (NumberFormatException e) {
            // Ignora se não for um número válido
        }
    }
    
    private void confirmarEntrada() {
        aguardandoNovoValor = true;
    }
    
    private void limparTudo() {
        campoTextoVisor.setText("0");
        inicializarRegistros();
        atualizarPainelInformacoes();
        aguardandoNovoValor = false;
        ultimaOperacao = "";
        valorAnterior = 0;
    }
    
    private void operacao(String op) {
        try {
            if (!ultimaOperacao.isEmpty() && !aguardandoNovoValor) {
                calcularResultado();
            } else {
                valorAnterior = Double.parseDouble(campoTextoVisor.getText());
            }
            ultimaOperacao = op;
            aguardandoNovoValor = true;
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido no visor");
        }
    }
    
    private void calcularResultado() {
        if (ultimaOperacao.isEmpty()) return;
        
        try {
            double valorAtual = Double.parseDouble(campoTextoVisor.getText());
            double resultado = 0;
            
            switch (ultimaOperacao) {
                case "+":
                    resultado = valorAnterior + valorAtual;
                    break;
                case "-":
                    resultado = valorAnterior - valorAtual;
                    break;
                case "*":
                    resultado = valorAnterior * valorAtual;
                    break;
                case "/":
                    if (valorAtual == 0) {
                        mostrarErro("Não é possível dividir por zero");
                        return;
                    }
                    resultado = valorAnterior / valorAtual;
                    break;
                default:
                    return;
            }
            
            campoTextoVisor.setText(formatarNumero(resultado));
            ultimaOperacao = "";
            aguardandoNovoValor = true;
            
        } catch (NumberFormatException e) {
            mostrarErro("Erro no cálculo");
        }
    }

    private void processarFuncaoFinanceira(String funcao) {
        try {
            String textoVisor = campoTextoVisor.getText();
            
            // Sempre tenta armazenar primeiro se há um valor válido no visor
            if (!textoVisor.trim().isEmpty() && !textoVisor.equals("0")) {
                try {
                    double valorAtual = Double.parseDouble(textoVisor);
                    registrosFinanceiros.put(funcao, valorAtual);
                    atualizarPainelInformacoes();
                    mostrarMensagem("Valor " + formatoDecimal.format(valorAtual) + " armazenado em " + funcao);
                    aguardandoNovoValor = true;
                    return;
                } catch (NumberFormatException e) {
                    // Se não conseguir converter, tenta calcular
                }
            }
            
            // Se chegou aqui, tenta calcular a função
            double resultado = calcularFuncaoFinanceira(funcao);
            registrosFinanceiros.put(funcao, resultado);
            campoTextoVisor.setText(formatarNumero(resultado));
            atualizarPainelInformacoes();
            aguardandoNovoValor = true;
            
        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
        }
    }
    
    /**
     * Calcula a taxa de juros mensal de um financiamento
     * usando apenas VF (Valor Futuro), VP (Valor Presente) e n (Número de períodos)
     * Fórmula: i = ((VF/VP)^(1/n)) - 1
     * @param vf Valor Futuro
     * @param vp Valor Presente
     * @param n Número de períodos
     * @return Taxa de juros mensal em percentual
     * @throws Exception se os parâmetros forem inválidos
     */
    private double calcularJurosMensal(double vf, double vp, double n) throws Exception {
        // Validações
        if (n <= 0) {
            throw new Exception("Número de períodos (n) deve ser maior que zero. Valor atual: " + n);
        }
        if (vp == 0) {
            throw new Exception("Valor Presente (VP) não pode ser zero");
        }
        if (vf == 0) {
            throw new Exception("Valor Futuro (VF) não pode ser zero");
        }
        if (vf * vp < 0) {
            throw new Exception("VF e VP devem ter sinais opostos (um positivo e outro negativo)");
        }
        
        // Calcula a taxa usando a fórmula de juros compostos: i = ((VF/VP)^(1/n)) - 1
        double razao = Math.abs(vf / vp);
        double taxa = Math.pow(razao, 1.0 / n) - 1;
        
        return taxa * 100; // Retorna em percentual
    }

    /**
     * Calcula o número de meses de um financiamento
     * usando apenas VF (Valor Futuro), VP (Valor Presente) e i (Taxa de juros ao mês)
     * Fórmula: n = ln(VF/VP) / ln(1 + i)
     * @param vf Valor Futuro
     * @param vp Valor Presente  
     * @param i Taxa de juros mensal (em decimal, não percentual)
     * @return Número de períodos (meses)
     * @throws Exception se os parâmetros forem inválidos
     */
    private double calcularQuantidadeMeses(double vf, double vp, double i) throws Exception {
        // Validações
        if (vp == 0) {
            throw new Exception("Valor Presente (VP) não pode ser zero");
        }
        if (vf == 0) {
            throw new Exception("Valor Futuro (VF) não pode ser zero");
        }
        if (vf * vp < 0) {
            throw new Exception("VF e VP devem ter sinais opostos (um positivo e outro negativo)");
        }
        if (i <= -1) {
            throw new Exception("Taxa de juros deve ser maior que -100%");
        }
        
        double razao = Math.abs(vf / vp);
        if (razao <= 1 && i >= 0) {
            throw new Exception("Com taxa positiva, VF deve ser maior que VP em valor absoluto");
        }
        if (razao >= 1 && i <= 0) {
            throw new Exception("Com taxa negativa, VP deve ser maior que VF em valor absoluto");
        }
        
        // Calcula o número de períodos: n = ln(VF/VP) / ln(1 + i)
        double numerador = Math.log(razao);
        double denominador = Math.log(1 + i);
        
        if (Math.abs(denominador) < 1e-10) {
            throw new Exception("Taxa muito próxima de zero para cálculo preciso");
        }
        
        return numerador / denominador;
    }

    /**
     * Método auxiliar para calcular juros mensal usando os registros atuais
     * Este método utiliza os valores armazenados nos registros VF, VP e n
     */
    private double calcularJurosMensalComRegistros() throws Exception {
        double vf = registrosFinanceiros.get(VF);
        double vp = registrosFinanceiros.get(VP);
        double n = registrosFinanceiros.get(PERIODO);
        
        if (vf == 0 && vp == 0 && n == 0) {
            throw new Exception("É necessário ter valores em VF, VP e n para calcular a taxa de juros");
        }
        
        return calcularJurosMensal(vf, vp, n);
    }

    /**
     * Método auxiliar para calcular quantidade de meses usando os registros atuais  
     * Este método utiliza os valores armazenados nos registros VF, VP e i
     */
    private double calcularQuantidadeMesesComRegistros() throws Exception {
        double vf = registrosFinanceiros.get(VF);
        double vp = registrosFinanceiros.get(VP);
        double i = registrosFinanceiros.get(TAXA) / 100.0; // Converte de percentual para decimal
        
        if (vf == 0 && vp == 0 && registrosFinanceiros.get(TAXA) == 0) {
            throw new Exception("É necessário ter valores em VF, VP e i para calcular o número de períodos");
        }
        
        return calcularQuantidadeMeses(vf, vp, i);
    }
    
    private double calcularFuncaoFinanceira(String funcao) throws Exception {
        double vp = registrosFinanceiros.get(VP);
        double vf = registrosFinanceiros.get(VF);
        double pmt = registrosFinanceiros.get(PMT);
        double i = registrosFinanceiros.get(TAXA) / 100.0; // Converte percentual para decimal
        double n = registrosFinanceiros.get(PERIODO);
        
        switch (funcao) {
            case VP:
                if (n <= 0) {
                    throw new Exception("Número de períodos (n) deve ser maior que zero para calcular VP. Valor atual: " + n);
                }
                if (i == 0) {
                    return -(vf + pmt * n);
                }
                return -(vf / Math.pow(1 + i, n) + pmt * ((1 - Math.pow(1 + i, -n)) / i));
                
            case VF:
                if (n <= 0) {
                    throw new Exception("Número de períodos (n) deve ser maior que zero para calcular VF. Valor atual: " + n);
                }
                if (i == 0) {
                    return -(vp + pmt * n);
                }
                return -(vp * Math.pow(1 + i, n) + pmt * ((Math.pow(1 + i, n) - 1) / i));
                
            case PMT:
                if (n <= 0) {
                    throw new Exception("Número de períodos (n) deve ser maior que zero para calcular PMT. Valor atual: " + n);
                }
                if (i == 0) {
                    return -(vp + vf) / n;
                }
                return -(vp * i * Math.pow(1 + i, n) + vf * i) / (Math.pow(1 + i, n) - 1);
                
            case TAXA:
                return calcularJurosMensalComRegistros();
                
            case PERIODO:
                return calcularQuantidadeMesesComRegistros();
                
            default:
                throw new Exception("Função financeira desconhecida: " + funcao);
        }
    }
    
    private double calcularTaxaIterativa(double vp, double vf, double pmt, double n) throws Exception {
        if (n <= 0) throw new Exception("Número de períodos deve ser maior que zero");
        
        double taxa = 0.1; // Estimativa inicial de 10%
        double precisao = 0.000001;
        int maxIteracoes = 100;
        
        for (int iter = 0; iter < maxIteracoes; iter++) {
            double fator = Math.pow(1 + taxa, n);
            double f = vp * fator + pmt * ((fator - 1) / taxa) + vf;
            
            if (Math.abs(f) < precisao) {
                return taxa;
            }
            
            // Derivada para método de Newton-Raphson
            double df = vp * n * Math.pow(1 + taxa, n - 1) + 
                       pmt * ((n * Math.pow(1 + taxa, n - 1) * taxa - (fator - 1)) / (taxa * taxa));
            
            if (Math.abs(df) < precisao) {
                throw new Exception("Não foi possível convergir para uma solução");
            }
            
            taxa = taxa - f / df;
            
            if (taxa < -1) taxa = -0.99; // Evita taxas menores que -100%
        }
        
        throw new Exception("Não foi possível calcular a taxa após " + maxIteracoes + " iterações");
    }
    
    private String formatarNumero(double numero) {
        if (Math.abs(numero) < 0.01 && numero != 0) {
            return String.format("%.6f", numero);
        }
        return formatoDecimal.format(numero);
    }
    
    private void atualizarPainelInformacoes() {
        Component[] componentes = ((JPanel) ((JPanel) getContentPane().getComponent(0))
                .getComponent(2)).getComponents();
        
        String[] registros = {VP, VF, PMT, TAXA, PERIODO};
        for (int i = 0; i < registros.length && i < componentes.length; i++) {
            if (componentes[i] instanceof JLabel) {
                JLabel label = (JLabel) componentes[i];
                double valor = registrosFinanceiros.get(registros[i]);
                String texto = registros[i] + ": " + formatarNumero(valor);
                if (registros[i].equals(TAXA)) {
                    texto += "%";
                }
                label.setText(texto);
            }
        }
    }
    
    private void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarErro(String erro) {
        JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculadoraHP().setVisible(true));
    }
    
}

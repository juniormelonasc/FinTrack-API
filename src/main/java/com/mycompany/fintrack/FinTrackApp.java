/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.fintrack;

/**
 *
 * @author Júnior Melo
 */


import controller.FinTracker;
import exceptions.EntradaInvalidaException;
import utils.Formatador;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class FinTrackApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FinTracker finTracker = new FinTracker();

    public static void main(String[] args) {
        System.out.println("===== BEM-VINDO AO FINTRACK =====");
        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();
            try {
                switch (opcao) {
                    case 1:
                        adicionarTransacao();
                        break;
                    case 2:
                        finTracker.listarTransacoes();
                        break;
                    case 3:
                        mostrarSaldo();
                        break;
                    case 4:
                        removerTransacao();
                        break;
                    case 5:
                        System.out.println("Encerrando FinTrack. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Escolha um número entre 1 e 5.");
                }
            } catch (EntradaInvalidaException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
            System.out.println(); // linha em branco para legibilidade
        } while (opcao != 5);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("===== FINTRACK - MENU PRINCIPAL =====");
        System.out.println("1. Adicionar nova transação");
        System.out.println("2. Listar transações");
        System.out.println("3. Mostrar saldo atual");
        System.out.println("4. Remover transação");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // opção inválida
        }
    }

    private static void adicionarTransacao() throws EntradaInvalidaException {
        System.out.println("\n--- Nova Transação ---");

        // Descrição
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();
        if (descricao.isEmpty()) {
            throw new EntradaInvalidaException("A descrição não pode ser vazia.");
        }

        // Valor
        double valor;
        try {
            System.out.print("Valor (use ponto como separador decimal): ");
            valor = Double.parseDouble(scanner.nextLine());
            if (valor <= 0) {
                throw new EntradaInvalidaException("O valor deve ser positivo.");
            }
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException("Valor inválido. Digite um número.");
        }

        // Tipo (receita/despesa)
        System.out.print("É receita? (S/N): ");
        String tipo = scanner.nextLine().trim().toUpperCase();
        boolean ehReceita;
        if (tipo.equals("S")) {
            ehReceita = true;
        } else if (tipo.equals("N")) {
            ehReceita = false;
        } else {
            throw new EntradaInvalidaException("Resposta inválida. Digite S ou N.");
        }

        // Data
        LocalDate data;
        try {
            System.out.print("Data (dd/MM/yyyy) - deixe em branco para hoje: ");
            String dataStr = scanner.nextLine().trim();
            if (dataStr.isEmpty()) {
                data = LocalDate.now();
            } else {
                String[] partes = dataStr.split("/");
                if (partes.length != 3) {
                    throw new EntradaInvalidaException("Formato de data inválido. Use dd/MM/yyyy.");
                }
                int dia = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int ano = Integer.parseInt(partes[2]);
                data = LocalDate.of(ano, mes, dia);
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new EntradaInvalidaException("Data inválida. Use números no formato dd/MM/yyyy.");
        }

        // Pergunta se é transação mensal
        System.out.print("É uma transação mensal/recorrente? (S/N): ");
        String mensal = scanner.nextLine().trim().toUpperCase();
        if (mensal.equals("S")) {
            System.out.print("Mês de referência (1 a 12): ");
            try {
                int mesRef = Integer.parseInt(scanner.nextLine());
                if (mesRef < 1 || mesRef > 12) {
                    throw new EntradaInvalidaException("Mês deve estar entre 1 e 12.");
                }
                finTracker.adicionarTransacaoMensal(descricao, valor, ehReceita, data, mesRef);
                System.out.println("Transação mensal adicionada com sucesso!");
            } catch (NumberFormatException e) {
                throw new EntradaInvalidaException("Mês inválido. Digite um número.");
            }
        } else {
            finTracker.adicionarTransacao(descricao, valor, ehReceita, data);
            System.out.println("Transação adicionada com sucesso!");
        }
    }

    private static void mostrarSaldo() {
        double saldo = finTracker.calcularSaldoTotal();
        System.out.println("\n--- SALDO ATUAL ---");
        System.out.println("Saldo: " + Formatador.formatarValor(saldo));
    }

    private static void removerTransacao() throws EntradaInvalidaException {
        if (!finTracker.temTransacoes()) {
            System.out.println("Não há transações para remover.");
            return;
        }

        finTracker.listarTransacoes();
        System.out.print("Digite o número da transação que deseja remover: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine());
            finTracker.removerTransacao(indice);
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException("Número inválido.");
        }
    }
}
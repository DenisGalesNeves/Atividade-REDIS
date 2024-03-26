package br.edu.fatec.tarefaRedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.Map;
import java.util.Scanner;

public class DemoApplication {

	private final Jedis jedis;

	public DemoApplication() {
	this.jedis = new Jedis("redis-16636.c308.sa-east-1-1.ec2.cloud.redislabs.com", 16636);
	jedis.auth("mrroy1Rkfm7SbDZ8as7dHsv4g7Ghen4j");
	}

	public void addTarefa(String id, String descricao) {
		jedis.hset("Tarefas", id, descricao);
	}

	public void listTarefas() {
		Map<String, String> tarefas = jedis.hgetAll("Tarefas");
		System.out.println("Lista das Tarefas:");
		for (Map.Entry<String, String> entry : tarefas.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	public void markTarefasConcluidas(String id) {
		String descricao = jedis.hget("Tarefas", id);
		if (descricao != null) {
			jedis.hset("Tarefas-Concluidas", id, descricao);
			jedis.hdel("Tarefas", id);
		} else {
			System.out.println("O ID da Tarefa é inválido.");
		}
	}

	public void listTarefasConcluidas() {
		Map<String, String> tarefasConcluidas = jedis.hgetAll("Tarefas-Concluidas");
		System.out.println("Lista das Tarefas Concluídas:");
		for (Map.Entry<String, String> entry : tarefasConcluidas.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	public void removeTarefasConcluidas() {
		jedis.del("Tarefas-Concluidas");
	}

	public static void main(String[] args) {
		DemoApplication taskManager = new DemoApplication();

        try (Scanner scanner = new Scanner(System.in)) {
            int opcao;
            do {
                System.out.println("=-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-=");
                System.out.println("Bem-vindo, por favor escolher uma opção abaixo:");
                System.out.println("=-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-=");
                System.out.println(" ");
                System.out.println("Digite 1 para Adicionar Tarefa");
                System.out.println("Digite 2 para Listar Tarefas");
                System.out.println("Digite 3 para Marcar Tarefa como Concluída");
                System.out.println("Digite 4 para Listar Tarefas Concluídas");
                System.out.println("Digite 5 para Remover Tarefas Concluídas");
                System.out.println("Digite 6 para Sair");
                System.out.println(" ");

                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.println(" ");
                        System.out.println("Digite o ID da tarefa:");
                        String id = scanner.nextLine();
                        System.out.println(" ");
                        System.out.println("Digite a descrição da tarefa:");
                        String descricao = scanner.nextLine();
                        System.out.println(" ");
                        taskManager.addTarefa(id, descricao);
                        break;
                    case 2:
                        taskManager.listTarefas();
                        break;
                    case 3:
                        System.out.println(" ");
                        System.out.println("Digite o ID da tarefa concluída:");
                        String idConcluida = scanner.nextLine();
                        System.out.println(" ");
                        taskManager.markTarefasConcluidas(idConcluida);
                        break;
                    case 4:
                        taskManager.listTarefasConcluidas();
                        break;
                    case 5:
                        taskManager.removeTarefasConcluidas();
                        System.out.println(" ");
                        System.out.println("As Tarefas concluídas foram removidas!");
                        System.out.println(" ");
                        break;
                    case 6:
                        System.out.println(" ");
                        System.out.println("Saindo");
                        break;
                    default:
                        System.out.println(" ");
                        System.out.println("Opção inválida, tente outro número.");
                }
            } while (opcao != 6);
        } catch (JedisConnectionException e) {
            System.out.println(" ");
            System.err.println("Não foi possível realizar a conexão ao servidor Redis. Verifique suas credenciais e conexão com a internet.");
        } finally {
            taskManager.jedis.close();
        }
	}
}

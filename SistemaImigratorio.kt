import java.text.SimpleDateFormat
import java.util.*

/**
 * SISTEMA DE CONTROLE IMIGRATÓRIO - VERSÃO BÁSICA
 * -------------------------------------------------
 * Projeto simples em Kotlin que simula um sistema de
 * controle de entrada e saída de viajantes em um país.
 *
 * Funcionalidades:
 *  1. Cadastrar viajante
 *  2. Registrar entrada no país
 *  3. Registrar saída do país
 *  4. Listar viajantes cadastrados
 *  5. Listar histórico de movimentações
 *  6. Sair do sistema
 */

// Classe que representa um viajante
data class Viajante(
    val nome: String,
    val passaporte: String,
    val nacionalidade: String,
    var status: String = "Fora do país" // pode ser "No país" ou "Fora do país"
)

// Classe que representa um registro de movimentação (entrada/saída)
data class Movimentacao(
    val passaporte: String,
    val tipo: String, // "ENTRADA" ou "SAIDA"
    val dataHora: String
)

class SistemaImigratorio {

    private val viajantes = mutableListOf<Viajante>()
    private val historico = mutableListOf<Movimentacao>()
    private val formatoData = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

    // Cadastra um novo viajante no sistema
    fun cadastrarViajante(nome: String, passaporte: String, nacionalidade: String) {
        val jaExiste = viajantes.any { it.passaporte == passaporte }
        if (jaExiste) {
            println("⚠ Já existe um viajante cadastrado com esse passaporte.")
            return
        }
        viajantes.add(Viajante(nome, passaporte, nacionalidade))
        println("✔ Viajante \"$nome\" cadastrado com sucesso!")
    }

    // Registra a entrada de um viajante no país
    fun registrarEntrada(passaporte: String) {
        val viajante = buscarViajante(passaporte) ?: return

        if (viajante.status == "No país") {
            println("⚠ Este viajante já está registrado como dentro do país.")
            return
        }

        viajante.status = "No país"
        registrarMovimentacao(passaporte, "ENTRADA")
        println("✔ Entrada registrada para ${viajante.nome}.")
    }

    // Registra a saída de um viajante do país
    fun registrarSaida(passaporte: String) {
        val viajante = buscarViajante(passaporte) ?: return

        if (viajante.status == "Fora do país") {
            println("⚠ Este viajante já está registrado como fora do país.")
            return
        }

        viajante.status = "Fora do país"
        registrarMovimentacao(passaporte, "SAIDA")
        println("✔ Saída registrada para ${viajante.nome}.")
    }

    // Lista todos os viajantes cadastrados e seus status atuais
    fun listarViajantes() {
        if (viajantes.isEmpty()) {
            println("Nenhum viajante cadastrado.")
            return
        }
        println("\n=== VIAJANTES CADASTRADOS ===")
        viajantes.forEach {
            println("Nome: ${it.nome} | Passaporte: ${it.passaporte} | Nacionalidade: ${it.nacionalidade} | Status: ${it.status}")
        }
    }

    // Lista o histórico completo de movimentações
    fun listarHistorico() {
        if (historico.isEmpty()) {
            println("Nenhuma movimentação registrada.")
            return
        }
        println("\n=== HISTÓRICO DE MOVIMENTAÇÕES ===")
        historico.forEach {
            val viajante = viajantes.find { v -> v.passaporte == it.passaporte }
            val nome = viajante?.nome ?: "Desconhecido"
            println("[${it.dataHora}] $nome (${it.passaporte}) -> ${it.tipo}")
        }
    }

    // Função auxiliar para buscar um viajante pelo passaporte
    private fun buscarViajante(passaporte: String): Viajante? {
        val viajante = viajantes.find { it.passaporte == passaporte }
        if (viajante == null) {
            println("⚠ Nenhum viajante encontrado com o passaporte informado.")
        }
        return viajante
    }

    // Função auxiliar para registrar uma movimentação com data/hora atual
    private fun registrarMovimentacao(passaporte: String, tipo: String) {
        val dataAtual = formatoData.format(Date())
        historico.add(Movimentacao(passaporte, tipo, dataAtual))
    }
}

// Função principal com menu interativo no console
fun main() {
    val sistema = SistemaImigratorio()
    val scanner = Scanner(System.`in`)
    var opcao: Int

    do {
        println("\n===== SISTEMA DE CONTROLE IMIGRATÓRIO =====")
        println("1 - Cadastrar viajante")
        println("2 - Registrar entrada no país")
        println("3 - Registrar saída do país")
        println("4 - Listar viajantes")
        println("5 - Listar histórico de movimentações")
        println("6 - Sair")
        print("Escolha uma opção: ")

        opcao = scanner.nextLine().toIntOrNull() ?: -1

        when (opcao) {
            1 -> {
                print("Nome do viajante: ")
                val nome = scanner.nextLine()
                print("Número do passaporte: ")
                val passaporte = scanner.nextLine()
                print("Nacionalidade: ")
                val nacionalidade = scanner.nextLine()
                sistema.cadastrarViajante(nome, passaporte, nacionalidade)
            }
            2 -> {
                print("Número do passaporte: ")
                val passaporte = scanner.nextLine()
                sistema.registrarEntrada(passaporte)
            }
            3 -> {
                print("Número do passaporte: ")
                val passaporte = scanner.nextLine()
                sistema.registrarSaida(passaporte)
            }
            4 -> sistema.listarViajantes()
            5 -> sistema.listarHistorico()
            6 -> println("Encerrando o sistema. Até logo!")
            else -> println("⚠ Opção inválida. Tente novamente.")
        }

    } while (opcao != 6)
}

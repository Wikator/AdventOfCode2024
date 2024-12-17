import kotlin.math.pow

enum class Opcode {
    ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV
}

fun main() {
    
    data class Instruction(val opcode: Opcode, val operand: Int)
    
    fun Char.toOpcode(): Opcode = when (this) {
        '0' -> Opcode.ADV
        '1' -> Opcode.BXL
        '2' -> Opcode.BST
        '3' -> Opcode.JNZ
        '4' -> Opcode.BXC
        '5' -> Opcode.OUT
        '6' -> Opcode.BDV
        '7' -> Opcode.CDV
        else -> throw IllegalArgumentException("Invalid opcode")
    }
    
    fun getRegister(input: List<String>, number: Int): Int {
        val relevantText = input.drop(number).first()
        return relevantText.split(": ").last().toInt()
    }
    
    fun getProgram(input: List<String>): List<Instruction> {
        val relevantText = input.last()

        val regex = Regex("\\d")
        val numbers = regex.findAll(relevantText).map { it.value.first() }
        val windowedNumbers = numbers.windowed(2, 2).toList()
        return windowedNumbers.map { Instruction(it.first().toOpcode(), it.last().digitToInt()) }
    }

    fun part1(input: List<String>): List<Int> {
        val initialRegisterA = getRegister(input, 0)
        val initialRegisterB = getRegister(input, 1)
        val initialRegisterC = getRegister(input, 2)
        val instructions = getProgram(input)
        
        tailrec fun execute(instructionPointer: Int, registerA: Int, registerB: Int, registerC: Int,
                            acc: List<Int>): List<Int> {
            
            if (instructionPointer >= instructions.size) return acc
            
            fun getComboOperand(operand: Int): Int = when (operand) {
                4 -> registerA
                5 -> registerB
                6 -> registerC
                7 -> throw IllegalArgumentException("Invalid combo operand")
                else -> operand
            }
            
            val instruction = instructions[instructionPointer]
            
            return when (instruction.opcode) {
                Opcode.ADV -> {
                    val denominator = 2.0.pow(getComboOperand(instruction.operand)).toInt()
                    val result = registerA / denominator
                    execute(instructionPointer + 1, result, registerB, registerC, acc)
                }
                Opcode.BXL -> {
                    val result = registerB.xor(instruction.operand)
                    execute(instructionPointer + 1, registerA, result, registerC, acc)
                }
                Opcode.BST -> {
                    val result = getComboOperand(instruction.operand) % 8
                    execute(instructionPointer + 1, registerA, result, registerC, acc)
                }
                Opcode.JNZ -> {
                    if (registerA == 0) {
                        execute(instructionPointer + 1, registerA, registerB, registerC, acc)
                    } else {
                        val newInstructionPointer = instruction.operand / 2
                        execute(newInstructionPointer, registerA, registerB, registerC, acc)
                    }
                }
                Opcode.BXC -> {
                    val result = registerB.xor(registerC)
                    execute(instructionPointer + 1, registerA, result, registerC, acc)
                }
                Opcode.OUT -> {
                    val result = getComboOperand(instruction.operand) % 8
                    execute(instructionPointer + 1, registerA, registerB, registerC, acc + result)
                }
                Opcode.BDV -> {
                    val denominator = 2.0.pow(getComboOperand(instruction.operand)).toInt()
                    val result = registerA / denominator
                    execute(instructionPointer + 1, registerA, result, registerC, acc)
                }
                Opcode.CDV -> {
                    val denominator = 2.0.pow(getComboOperand(instruction.operand)).toInt()
                    val result = registerA / denominator
                    execute(instructionPointer + 1, registerA, registerB, result, acc)
                }
            }
        }
        
        return execute(0, initialRegisterA, initialRegisterB, initialRegisterC, emptyList())
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day17")
    part1(input).joinToString(",").println()
    part2(input).println()
}

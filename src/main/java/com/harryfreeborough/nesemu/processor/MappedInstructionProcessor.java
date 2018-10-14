package com.harryfreeborough.nesemu.processor;

import com.harryfreeborough.nesemu.MemoryBus;
import com.harryfreeborough.nesemu.CpuState;
import com.harryfreeborough.nesemu.addressing.InstructionMode;
import com.harryfreeborough.nesemu.instructions.Instruction;
import com.harryfreeborough.nesemu.instructions.arithmetic.InstructionAdc;
import com.harryfreeborough.nesemu.instructions.bitwise.InstructionAnd;
import com.harryfreeborough.nesemu.instructions.bitwise.InstructionAsl;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBcc;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBcs;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBeq;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBmi;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBne;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBpl;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBvc;
import com.harryfreeborough.nesemu.instructions.branch.InstructionBvs;
import com.harryfreeborough.nesemu.instructions.execution.InstructionJmp;
import com.harryfreeborough.nesemu.instructions.execution.InstructionJsr;
import com.harryfreeborough.nesemu.instructions.execution.InstructionRts;
import com.harryfreeborough.nesemu.instructions.flags.InstructionCld;
import com.harryfreeborough.nesemu.instructions.registers.InstructionIny;
import com.harryfreeborough.nesemu.instructions.registers.InstructionLda;
import com.harryfreeborough.nesemu.instructions.registers.InstructionLdx;
import com.harryfreeborough.nesemu.instructions.registers.InstructionLdy;
import com.harryfreeborough.nesemu.instructions.registers.InstructionSta;
import com.harryfreeborough.nesemu.instructions.stack.InstructionTxs;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

import java.util.HashMap;

import static com.harryfreeborough.nesemu.utils.Preconditions.checkNotNull;
import static com.harryfreeborough.nesemu.utils.Preconditions.checkState;

public class MappedInstructionProcessor implements InstructionProcessor {

    private final CpuState store;
    private final MemoryBus bus;
    private final HashMap<Integer, Instruction> instructions;

    public MappedInstructionProcessor(CpuState store, MemoryBus bus) {
        this.store = store;
        this.bus = bus;

        this.instructions = new HashMap<>();
        this.addInstructions(
                new InstructionAdc(), new InstructionAnd(), new InstructionAsl(),
                new InstructionCld(), new InstructionJmp(), new InstructionJsr(),
                new InstructionLda(), new InstructionLdx(), new InstructionLdy(),
                new InstructionRts(), new InstructionSta(), new InstructionTxs(),
                new InstructionBcc(), new InstructionBcs(), new InstructionBeq(),
                new InstructionBmi(), new InstructionBne(), new InstructionBpl(),
                new InstructionBvc(), new InstructionBvs(), new InstructionIny()
        );
    }

    public void addInstructions(Instruction... instructions) {
        for (Instruction instruction : instructions) {
            for (int opCode : instruction.getOpCodes()) {
                checkState(!this.instructions.containsKey(opCode), "Duplicate OPCODE: 0x%02X", opCode);
                this.instructions.put(opCode, instruction);
            }
        }
    }

    @Override
    public void process() {
        int opCode = MemoryUtils.programPop1(this.bus, this.store);

        Instruction instruction = this.instructions.get(opCode);
        checkNotNull(instruction, "Failed to find InstructionMode for instruction with OPCODE: 0x%02X", opCode);

        InstructionMode opMode;
        if (instruction.obtainMode()) {
            opMode = this.getInstructionMode(opCode);
        } else {
            opMode = InstructionMode.IGNORED;
        }

        instruction.execute(opCode, opMode, this.bus, this.store);
    }

    public InstructionMode getInstructionMode(int opCode) {
        int addressingMode = (opCode & 0b00011100) >> 2;
        //There are 3 different instruction types which have different values
        //corresponding to different addressing modes
        int opType = opCode & 0b00000011;

        InstructionMode opMode = null;
        switch (opType) {
            case 0b00:
            case 0b10: {
                switch (addressingMode) {
                    case 0b000: //Immediate
                        opMode = InstructionMode.IMMEDIATE;
                        break;
                    case 0b001: //Zero Page
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop1(this.bus, this.store)
                        );
                        break;
                    case 0b010: //Accumulator
                        opMode = InstructionMode.ACCUMULATOR;
                        break;
                    case 0b011: //Absolute
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop2(this.bus, this.store)
                        );
                        break;
                    case 0b100: //Not used, ignored
                        break;
                    case 0b101: //Zero Page X
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop1(this.bus, this.store) + this.store.regX
                        );
                        break;
                    case 0b110: //Accumulator
                        opMode = InstructionMode.ACCUMULATOR;
                        break;
                    case 0b111: //Absolute X
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop2(this.bus, this.store) + this.store.regX
                        );
                        break;
                }
                break;
            }

            case 0b01: {
                switch (addressingMode) {
                    case 0b000: //Indirect Zero Page X
                        opMode = InstructionMode.createInMemoryMode(
                                this.bus.read2(MemoryUtils.programPop1(this.bus, this.store) + this.store.regX)
                        );
                        break;
                    case 0b001: //Zero Page
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop1(this.bus, this.store)
                        );
                        break;
                    case 0b010: //Immediate
                        opMode = InstructionMode.IMMEDIATE;
                        break;
                    case 0b011: //Absolute
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop2(this.bus, this.store)
                        );
                        break;
                    case 0b100: //Indirect Zero Page Y
                        opMode = InstructionMode.createInMemoryMode(
                                this.bus.read2(MemoryUtils.programPop1(this.bus, this.store)) + this.store.regY
                        );
                        break;
                    case 0b101: //Zero Page X
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop1(this.bus, this.store) + this.store.regX
                        );
                        break;
                    case 0b110: //Absolute Y
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop2(this.bus, this.store) + this.store.regY
                        );
                        break;
                    case 0b111: //Absolute X
                        opMode = InstructionMode.createInMemoryMode(
                                MemoryUtils.programPop2(this.bus, this.store) + this.store.regX
                        );
                        break;
                }

                break;
            }

            case 0b11: //Never used
                break;
        }


        return opMode;
    }

}

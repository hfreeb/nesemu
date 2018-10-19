package com.harryfreeborough.nesemu;

import com.harryfreeborough.nesemu.instruction.Command;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

import java.util.Arrays;

public class Cpu {

    private final MemoryBus bus;
    private final CpuState state;

    public Cpu(MemoryBus bus, CpuState state) {
        this.bus = bus;
        this.state = state;
    }
    
    public boolean run() {
        System.out.println(String.format("A: 0x%X, X: 0x%X, Y: 0x%X, S: 0x%X, PC: 0x%X",
                state.regA, state.regX, state.regY, state.regSp, state.regPc));

        int opcode = MemoryUtils.programPop1(this.bus, this.state);

        if (opcode == 0) {
            System.out.println("HALTING");
            return false;
        }

        Command command = Arrays.stream(Command.values())
                .filter(i -> i.getOpcode() == opcode)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Failed to find instruction with opcode: 0x%X", opcode)
                ));

        int arg1 = this.bus.read1(this.state.regPc);
        int arg2 = this.bus.read1(this.state.regPc + 1);

        String format = command.getAddressingMode().getFormat()
                .replace("$[1,2]", String.format("$%04X", arg1 | (arg2 << 8)))
                .replace("$[1]", String.format("$%02X", arg1))
                .replace("[1]", String.format("%d", MemoryUtils.signedByteToInt(arg1)));

        System.out.println(String.format("Processing %s %s", command.getInstruction().name(), format));

        command.getInstruction().getProcessor().execute(this, command.getAddressingMode());

        return true;
    }

    public MemoryBus getBus() {
        return bus;
    }

    public CpuState getState() {
        return state;
    }

}

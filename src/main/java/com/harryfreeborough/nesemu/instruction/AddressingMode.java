package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.Cpu;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

public enum AddressingMode {

    ACCUMULATOR("A") {
        @Override
        public int read1(Cpu cpu) {
            return cpu.getState().regA;
        }

        @Override
        public void write1(Cpu cpu, int value) {
            cpu.getState().regA = value;
        }
    },
    IMPLIED(""),
    IMMEDIATE("#~1") {
        @Override
        public int read1(Cpu cpu) {
            return MemoryUtils.programPop1(cpu.getBus(), cpu.getState());
        }

        @Override
        public int obtainAddress(Cpu cpu) {
            return cpu.getState().regPc;
        }
    },
    ABSOLUTE("$~1~2") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return MemoryUtils.programPop2(cpu.getBus(), cpu.getState());
        }
    },
    ZERO_PAGE("$~1") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return MemoryUtils.programPop1(cpu.getBus(), cpu.getState());
        }
    },
    RELATIVE("*+~1") {
        @Override
        public int obtainAddress(Cpu cpu) {
            int value = (MemoryUtils.programPop1(cpu.getBus(), cpu.getState()) + cpu.getState().regPc) & 0xFFFF;
            return MemoryUtils.signedByteToInt(value);
        }
    };

    private final String format;

    AddressingMode(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public int obtainAddress(Cpu cpu) {
        throw new UnsupportedOperationException("obtainAddress not implemented for " + name() + " address mode.");
    }

    public int read1(Cpu cpu) {
        return cpu.getBus().read1(obtainAddress(cpu));
    }

    public void write1(Cpu cpu, int value) {
        cpu.getBus().write1(obtainAddress(cpu), value);
    }

}
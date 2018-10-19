package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.Cpu;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

//TODO: $ prefix should be hexadecimal, # is ltieral number
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
    IMP(""),
    IMM("#[1]") {
        @Override
        public int read1(Cpu cpu) {
            return MemoryUtils.programPop1(cpu.getBus(), cpu.getState());
        }

        @Override
        public int obtainAddress(Cpu cpu) {
            return cpu.getState().regPc;
        }
    },
    ABS("$[1,2]") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return MemoryUtils.programPop2(cpu.getBus(), cpu.getState());
        }
    },
    ABX("$[1,2],X") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return (MemoryUtils.programPop2(cpu.getBus(), cpu.getState()) + cpu.getState().regX) & 0xFFFF;
        }
    },
    ZPG("$[1]") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return MemoryUtils.programPop1(cpu.getBus(), cpu.getState());
        }
    },
    ZPX("$[1],X") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return (MemoryUtils.programPop1(cpu.getBus(), cpu.getState()) + cpu.getState().regX) & 0xFF;
        }
    },
    ZPY("$[1],Y") {
        @Override
        public int obtainAddress(Cpu cpu) {
            return (MemoryUtils.programPop1(cpu.getBus(), cpu.getState()) + cpu.getState().regY) & 0xFF;
        }
    },
    REL("*[1]") {
        @Override
        public int obtainAddress(Cpu cpu) {
            int offset = MemoryUtils.signedByteToInt(MemoryUtils.programPop1(cpu.getBus(), cpu.getState()));
            return (cpu.getState().regPc + offset) & 0xFFFF;
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

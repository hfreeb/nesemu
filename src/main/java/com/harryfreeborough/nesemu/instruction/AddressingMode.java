package com.harryfreeborough.nesemu.instruction;

import com.harryfreeborough.nesemu.cpu.CpuMemory;
import com.harryfreeborough.nesemu.cpu.CpuState;
import com.harryfreeborough.nesemu.utils.MemoryUtils;

/**
 * Represents all addressing modes an {@link Operation} can be.
 */
public enum AddressingMode {

    ACC("A") {
        @Override
        public int read1(CpuMemory bus, CpuState state) {
            return state.regA;
        }

        @Override
        public void write1(CpuMemory bus, CpuState state, int value) {
            state.regA = value;
        }
    },
    IMP(""),
    IMM("#$[1]") {
        @Override
        public int read1(CpuMemory bus, CpuState state) {
            return MemoryUtils.programPop1(bus, state);
        }

        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            return state.regPc;
        }

        @Override
        public void write1(CpuMemory bus, CpuState state, int value) {
            throw new UnsupportedOperationException("You can not write in immediate mode.");
        }
    },
    ABS("$[1,2]") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            return MemoryUtils.programPop2(bus, state);
        }
    },
    //TODO: ASL, LSR, ROL, ROR, DEC and INC should always take 7 cycles,
    //TODO: should probably have a ABX_BUG enum
    ABX("$[1,2],X") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            int popped = MemoryUtils.programPop2(bus, state);
            int address = (popped + state.regX) & 0xFFFF;
            if ((popped & 0xFF00) != (address & 0xFF00)) { //Different page
                state.cycles++;
            }
            return address;
        }
    },
    ABY("$[1,2],Y") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            int popped = MemoryUtils.programPop2(bus, state);
            int address = (popped + state.regY) & 0xFFFF;
            if ((popped & 0xFF00) != (address & 0xFF00)) { //Different page
                state.cycles++;
            }
            return address;
        }
    },
    ZPG("$[1]") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            return MemoryUtils.programPop1(bus, state);
        }
    },
    ZPX("$[1],X") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            return (MemoryUtils.programPop1(bus, state) + state.regX) & 0xFF;
        }
    },
    ZPY("$[1],Y") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            return (MemoryUtils.programPop1(bus, state) + state.regY) & 0xFF;
        }
    },
    REL("*[1]") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            int offset = MemoryUtils.signedByteToInt(MemoryUtils.programPop1(bus, state));
            return (state.regPc + offset) & 0xFFFF;
        }
    },
    IND("($[1,2])") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            return bus.read2Bug(MemoryUtils.programPop2(bus, state));
        }
    },
    IDX("($[1],X)") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            int address = (MemoryUtils.programPop1(bus, state) + state.regX) & 0xFF;
            return bus.read2Bug(address);
        }
    },
    IDY("($[1]),Y") {
        @Override
        public int obtainAddress(CpuMemory bus, CpuState state) {
            int popped = MemoryUtils.programPop1(bus, state);
            int address = bus.read2Bug(popped);
            int indexed = (address + state.regY) & 0xFFFF;
            if ((address & 0xFF00) != (indexed & 0xFF00)) {
                state.cycles++;
            }
            return indexed;
        }
    };

    private final String format;

    AddressingMode(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public int obtainAddress(CpuMemory bus, CpuState state) {
        return state.regMar; //Do nothing by default (specifically for ACC, IMP modes)
    }

    public int read1(CpuMemory bus, CpuState state) {
        return bus.read1(state.regMar);
    }

    public void write1(CpuMemory bus, CpuState state, int value) {
        bus.write1(state.regMar, value);
    }

}

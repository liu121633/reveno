package org.reveno.atp.clustering.core.fastcast;

import org.nustaq.offheap.bytez.Bytez;
import org.reveno.atp.core.api.channel.Buffer;

import java.nio.ByteBuffer;

public class BytezBufferWrapper implements Buffer {

    @Override
    public int readerPosition() {
        return position;
    }

    @Override
    public int writerPosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int limit() {
        return length;
    }

    @Override
    public long capacity() {
        return length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public int remaining() {
        return length - position;
    }

    @Override
    public void clear() {
    }

    @Override
    public void release() {
    }

    @Override
    public boolean isAvailable() {
        return remaining() > 0;
    }

    @Override
    public void setReaderPosition(int position) {
        this.position = position;
    }

    @Override
    public void setWriterPosition(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeByte(byte b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(byte[] bytes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(byte[] buffer, int offset, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeLong(long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeInt(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeShort(short s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeFromBuffer(ByteBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuffer writeToBuffer() {
        byte[] bytes = readBytes(length - position);
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public byte readByte() {
        byte b = bytez.get(off + position);
        position++;
        return b;
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] data = new byte[length];
        readBytes(data, 0, length);
        return data;
    }

    @Override
    public void readBytes(byte[] data, int offset, int length) {
        bytez.getArr(off + position, data, offset, length);
        position += length;
    }

    @Override
    public long readLong() {
        long l = bytez.getLong(off + position);
        position += 8;
        return l;
    }

    @Override
    public int readInt() {
        int i = bytez.getInt(off + position);
        position += 4;
        return i;
    }

    @Override
    public short readShort() {
        short s = bytez.getShort(off + position);
        position += 2;
        return s;
    }

    @Override
    public void markReader() {
    }

    @Override
    public void markWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetReader() {
    }

    @Override
    public void resetWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void limitNext(int count) {
    }

    @Override
    public void resetNextLimit() {
    }

    @Override
    public void markSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int sizeMarkPosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeSize() {
        throw new UnsupportedOperationException();
    }

    public Bytez getBytez() {
        return bytez;
    }

    public void setBytez(Bytez bytez, long off, int len) {
        this.bytez = bytez;
        this.off = off;
        this.length = len;
        this.position = 0;
    }

    protected Bytez bytez;
    protected long off;
    protected int length;

    protected int position;
}

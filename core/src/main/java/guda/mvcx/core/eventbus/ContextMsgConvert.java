package guda.mvcx.core.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.*;

/**
 * Created by well on 2017/3/25.
 */
public class ContextMsgConvert implements MessageCodec<HttpEventContext, HttpEventContext> {

    @Override
    public void encodeToWire(Buffer buffer, HttpEventContext httpEventContext) {
        long start=System.currentTimeMillis();
        System.out.println("convert:"+start);
        System.out.println("convert:"+httpEventContext);
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(httpEventContext);
            o.close();
            buffer.appendBytes(b.toByteArray());
            System.out.println("convert end:"+(System.currentTimeMillis()-start));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HttpEventContext decodeFromWire(int pos, Buffer buffer) {
        long start=System.currentTimeMillis();
        System.out.println("decode:"+start);
        final ByteArrayInputStream b = new ByteArrayInputStream(buffer.getBytes());
        ObjectInputStream o = null;
        HttpEventContext msg = null;
        try {
            o = new ObjectInputStream(b);
            msg = (HttpEventContext) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("decode end:"+(System.currentTimeMillis()-start));
        return msg;
    }

    @Override
    public HttpEventContext transform(HttpEventContext context) {
        return context;
    }

    @Override
    public String name() {
        return "DataMessage";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}

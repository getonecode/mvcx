package guda.mvcx.core.eventbus.msg;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.*;

/**
 * Created by well on 2017/3/25.
 */
public class HttpMsgConvert implements MessageCodec<HttpEventMsg, HttpEventMsg> {

    @Override
    public void encodeToWire(Buffer buffer, HttpEventMsg httpEventMsg) {
        long start=System.currentTimeMillis();
        System.out.println("convert:"+start);
        System.out.println("convert:"+ httpEventMsg);
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(httpEventMsg);
            o.close();
            buffer.appendBytes(b.toByteArray());
            System.out.println("convert end:"+(System.currentTimeMillis()-start));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HttpEventMsg decodeFromWire(int pos, Buffer buffer) {
        long start=System.currentTimeMillis();
        System.out.println("decode:"+start);
        final ByteArrayInputStream b = new ByteArrayInputStream(buffer.getBytes());
        ObjectInputStream o = null;
        HttpEventMsg msg = null;
        try {
            o = new ObjectInputStream(b);
            msg = (HttpEventMsg) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("decode end:"+(System.currentTimeMillis()-start));
        return msg;
    }

    @Override
    public HttpEventMsg transform(HttpEventMsg context) {
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

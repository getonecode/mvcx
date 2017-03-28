package guda.mvcx.core.eventbus.msg;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by well on 2017/3/25.
 */
public class HttpMsgConvert implements MessageCodec<HttpEventMsg, HttpEventMsg> {


    private Logger log = LoggerFactory.getLogger(HttpMsgConvert.class);

    @Override
    public void encodeToWire(Buffer buffer, HttpEventMsg httpEventMsg) {
        long start = System.currentTimeMillis();
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(httpEventMsg);
            o.close();
            buffer.appendBytes(b.toByteArray());
            if (log.isInfoEnabled()) {
                log.info("convert end:" + (System.currentTimeMillis() - start));
            }
        } catch (IOException e) {
            log.error("convert httpEventMsg error", e);
        }
    }

    @Override
    public HttpEventMsg decodeFromWire(int pos, Buffer buffer) {
        long start = System.currentTimeMillis();
        final ByteArrayInputStream b = new ByteArrayInputStream(buffer.getBytes());
        ObjectInputStream o = null;
        HttpEventMsg msg = null;
        try {
            o = new ObjectInputStream(b);
            msg = (HttpEventMsg) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("decode httpEventMsg error", e);
        }
        if (log.isInfoEnabled()) {
            log.info("decode end:" + (System.currentTimeMillis() - start));
        }
        return msg;
    }

    @Override
    public HttpEventMsg transform(HttpEventMsg context) {
        return context;
    }

    @Override
    public String name() {
        return "httpEventMsg";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}

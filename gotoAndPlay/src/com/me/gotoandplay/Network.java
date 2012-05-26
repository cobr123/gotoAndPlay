package com.me.gotoandplay;

import com.esotericsoftware.kryo.Kryo;

public class Network {
    public static final int port = 55777;

    public static void register (Kryo kryo) {
       kryo.register(Key.class);
       kryo.register(Position.class);
       kryo.register(MsgType.class);
       kryo.register(Message.class);
       kryo.register(Remove.class);
    }
}

package com.youhaoxi.livelink.protocol.protobuf.demo;

public class Demo {
    public static void main(String[] args) throws Exception{

        AddressBookProtos.Person john =
                AddressBookProtos.Person.newBuilder()
                        .setId(1234)
                        .setName("John Doe")
                        .setEmail("jdoe@example.com")
                        .addPhones(
                                AddressBookProtos.Person.PhoneNumber.newBuilder()
                                        .setNumber("555-4321")
                                        .setType(AddressBookProtos.Person.PhoneType.HOME))
                        .addPhones(AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("382-3321")
                                .setType(AddressBookProtos.Person.PhoneType.HOME))
                        .build();

        //序列化 转换成byte
        byte[] bytes = john.toByteArray();
        System.out.println("序列化成 bytes = " + bytes.length);

        //反序列化 转换成
        AddressBookProtos.Person person =  AddressBookProtos.Person.parseFrom(bytes);
        System.out.println("反序列化 person\n" + person.toString());

    }
}

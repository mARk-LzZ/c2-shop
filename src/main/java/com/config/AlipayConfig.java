package com.config;


import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {
    public final String APP_ID = "2021000117649535";

    public final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoQny66WssT+6CEARoM9/8DfTUjZALpDP5q447BxVk68gJNya0QyJ4GsnflPtpZklMiq2MbvYoma+QDFLkGGgl0ceNKJmG/GGyyQmGmbqcS/BLVQjbaT41SBU3twdweDpV6y7bw0XeL0KFarLWC2cSySK/iueZZ5tXuMbdsdqAhk8WvUQDN48J5jQjLrzhIi6loP2E1jwgE6Yy7BOcUkXZ3XsMt5b2LxML3FVvEvmj8ofrOBobbv85SJWQ4KEZN2AD+JfvJV9rdX/HgcyFAj++N503CEYR6AH/rrTUqpRD4w2gZvOuUfxJGA0bXeFOpq2eUu/naCYFaL1wk3bsg4PbAgMBAAECggEAVbCGlSoKyqZqcRJ3LBo/F76D0TvHACa/0YbtCGKzIpJxbUAJFgSaqU/7P0JAR40wBysY90wLUOHz5whfroo/gAM1/U2P4o7lsodvsnzX7IE//x1vRU5zv+7V32n2//A6xUbgtkrcairnVcrhCOEFvStx5tp22ElLc4FGxeCtxoXIp/6fKd8NdbQTSdabByCIE2457eYcaDpVXwq6jMYfojOz6g0swme6/rSpdhJjOwV/DJfJYYQCp1Nwwi+x29cpzlgAOYUlYD1kunQJkeBZPiE5NljDEEemU3JUfD4CDEQpgIoaJfepMgDn8mJUrvhgH+qx2kwwrdDT4YPDPZRLAQKBgQD7wf/U1WvEf652wdFTjXdsUyVi6NkKysySZCFSXbmHAtFP0fSh/4t8DnyWXUowlCiEh1138tltcpaOC2W69ExK2xyX1F8Tj8LRcbergwa0d2ErWKcmmj037BwzrmXu+R3MgfzcRawYC9lBBAspY/YOSdnxrpl9Oug6RXZUXzjaBwKBgQCrGE3ybWFVjGo7jjShTk0gqm1D/yYaZYxPv1QCG34kZAfTn2LQBu0+VZ1KZn62ThC/Q0ewM0f6APErb6HFaVQXFLgNtxUXyElYvK/V2yBGkES3ZP3ElUiu3pzAm7Hh1qCEFuaSvqmzz/HGDW0sIBjSMkXN1K4tId6BT0IpKy2ijQKBgFv09n9FHUH+6TjTfqQoLhDRJfzE1FJO1P47uLgfJ98atl2TcOmjL822dOdvV+P6mfoef5OKjsSdcESeASCSjzNgaUdD+R5qClz6Tn5FM7PDTyjIDvXYrLe6dGqkLvNk5u+GI1hJ0pxN56MJ2RHbQ0rgysyuW74UQDLYoGodBeUNAoGBAJQMSt58gV3qS75O9gGiZKDgd/3/mXP77ObQsxvDuGo62H7pCzLg24c1xA4uFHUhpeIqkKzKIv0UtZsVVJEi7C2wsvbDNpBGshkD5M8TdqE4kw1yaRgi6SrP3CnaT5kRBAJoYooWS3gZdqce1Rn6iB/Seo55+2F4TjXsZF6FS069AoGBAIarVpDdgDj+xYDRLSjjbMSWZAYI45htb6/pyRMD00/Mh8JimKi+lbtTNOo1u8TB7gmZwQ3PQMny5SXlQBr0xhgn7WXBuHr/4QfZXVq989j47snFP+vcUdztDrReYOC/MDgZBXKMumTiWaTNLq99YuxzKZuYsDryxYm7pKVtl0lx";
    public final String CHARSET = "UTF-8";
    public final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiC5/rV7RPbR4P4p03kaFUrsdcnBxf8Vv69JP5vGCFJJ9g1RhZPD/+WjqeAoau2euWSkVZoCI5GUsAylsYtySVDOKEkmnHPEO8rFKYzalUlU5ckMa1ZKgJgfI9E3O1XDohWZQDz5YxCPMVO5+Uyu2r28uGsL+f6s5b9gazjlY8ynvOfbKHHEHxIcdAsm5Zfxh7TjDZLn1Fz8j1T6AJl/S5vWsIrc/Z4iG4R5CMZVBm1KXsGS3ZOh3cCTT35v0stb37NS1XGdZG+kCf1KfnPdMlYCLhUeEuqCaWwwZ0KZCy0uNcuNfuWFRGWsdZdzvgj9oNvSQVl10ZFSQsIKyJfGuXQIDAQAB";
    //????????????????????????,???????????????https://openapi.alipay.com/gateway.do
    public final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
    public final String FORMAT = "JSON";
    //????????????
    public final String SIGN_TYPE = "RSA2";
    //???????????????????????????,????????????????????????????????????????????????,?????????????????????
    public final String NOTIFY_URL = "http://8.141.56.170/notifyUrl";
    //???????????????????????????,???????????????????????????????????????????????????,????????????????????????
    public final String RETURN_URL = "http://8.141.56.170/returnUrl";
}

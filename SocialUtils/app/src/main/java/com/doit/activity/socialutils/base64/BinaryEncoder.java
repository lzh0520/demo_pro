package com.doit.activity.socialutils.base64;


public interface BinaryEncoder extends Decoder {

	byte[] decode(byte[] source) throws DecoderException;
}

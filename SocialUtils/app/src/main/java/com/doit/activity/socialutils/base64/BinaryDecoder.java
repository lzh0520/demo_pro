package com.doit.activity.socialutils.base64;


public interface BinaryDecoder extends Decoder {

	byte[] decode(byte[] source) throws DecoderException;
}

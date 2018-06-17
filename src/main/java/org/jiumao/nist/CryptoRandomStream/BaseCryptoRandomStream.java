//
// Creator:    http://www.dicelocksecurity.com
// Version:    vers.5.0.0.1
//
// Copyright 2011 DiceLock Security, LLC. All rights reserved.
//
//                               DISCLAIMER
//
// THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
// OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// 
// DICELOCK IS A REGISTERED TRADEMARK OR TRADEMARK OF THE OWNERS.
// 
// Environment:
// java version "1.6.0_29"
// Java(TM) SE Runtime Environment (build 1.6.0_29-b11)
// Java HotSpot(TM) Server VM (build 20.4-b02, mixed mode)
// 

package org.jiumao.nist.CryptoRandomStream;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;

import org.jiumao.nist.Base.TypeSizes;

/**
 * Base class of crypto random streams
 * 
 * @author      Angel Ferré @ DiceLock Security
 * @version     5.0.0.1
 * @since       2011-09-28
 */
public abstract class BaseCryptoRandomStream {

    /**
     * Buffer to access CryptoRandomStream data
     */
    public ByteBuffer cryptoBuffer;
    /**
     * Length of CryptoRandomStream data in bits
     */
    public int bitLength;
    /**
     * Length of selected CryptoRandomStream data in bits if CryptoRandomStream has been reduced
     */
    public int reducedBitLength;
    /**
     * Current bit position of CryptoRandomStream
     */
    public int position;              // Current bit position

    /**
     * Inserts a bit in a byte at specified position, position goes from to 7. 
     * 
     * @param      byteInput   Byte to insert in a bit 
     * @param      bitPosition Position in byte where it will be isnerted the bit, from 0 to 7
     * @param      bitValue    Bit to be inserted
     * @return     byte: Byte with bit inserted
     */
    private byte SetByteBitPosition(byte byteInput, int bitPosition, byte bitValue) {

        switch (bitValue) {
        case 0:
            byteInput &= ~(1 << bitPosition);
            break;
        case 1:
            byteInput |= (1 << bitPosition);
            break;
        }
        return byteInput;
    }

  /**
   * Get the bit of a byte at specified position, position goes from to 7. 
   * 
   * @param      byteInput   Byte to get from the bit 
   * @param      bitPosition Position in byte where it's the bit, from 0 to 7
   * @return     byte:       Byte with bit value (0 or 1)
   */
    private byte GetByteBitPosition(byte byteInput, int bitPosition) {
        byte output;
      
        output = 0;
        output |= ((byteInput >> bitPosition) & 0x01);
        return output;
    }

  /**
   * Constructor, default
   * 
   */
    public BaseCryptoRandomStream() {

        this.bitLength = 0;
        this.reducedBitLength = 0;
        this.position = 0;
        this.cryptoBuffer = null;
    }

  /**
   * Destructor, zeroes all data
   * 
   */
    abstract protected void finalize();

  /**
   * Sets the BaseCryptoRandomStream position, minimum value 0
   * 
   * @param      position  Sets current CryptoRandomStreambit position at position parameter
   */
    public void SetBitPosition(int position) {

        this.position = position;
    }

  /**
   * Gets the current bit position.
   *
   * @return int:  The current bit position within CryptoRandomStream
   */
    public int GetBitPosition() {

        return this.position;
    }

  /**
   * Gets the stream length in bits
   * 
   * @return     int:  The current bit position within CryptoRandomStream
   */
    public int GetBitLength() {

        if ( this.reducedBitLength != 0 ) {
            return this.reducedBitLength;
        } else {
            return this.bitLength;
        }
    }

  /**
   * Gets the stream length in unsigned char type
   * 
   * @return     int:  CryptoRandomStream length in bytes (8 bits)
   */
    public int GetUCLength() {

        if ( this.reducedBitLength != 0 ) {
            return (this.reducedBitLength / TypeSizes.BYTE_BITS);
        } else {
            return (this.bitLength / TypeSizes.BYTE_BITS);
        }
    }

  /**
   * Gets the stream length in unsigned short type
   * 
   * @return     int:  CryptoRandomStream length in short integers (16 bits)
   */
    public int GetUSLength() {

        if ( this.reducedBitLength != 0 ) {
        	return (this.reducedBitLength / TypeSizes.SHORT_BITS);
        } else {
            return (this.bitLength / TypeSizes.SHORT_BITS);
        }
    }

  /**
   * Gets the stream length in unsigned long type
   * 
   * @return     int:  CryptoRandomStream length in long integers (32 bits)
   */
    public int GetULLength() {

        if ( this.reducedBitLength != 0 ) {
        	return (this.reducedBitLength / TypeSizes.INT_BITS);
        } else {
            return (this.bitLength / TypeSizes.INT_BITS);
        }
    }

  /**
   * Sets an empty stream with the indicated length in bits
   * 
   * @param      bitLength  length in bits of created CryptoRandomStream
   */
    abstract public void SetCryptoRandomStreamBit(int bitLength);

  /**
   * Sets an empty stream with the indicated length in unsigned chars
   * 
   * @param      ucLength  length in bytes (8 bits) of created CryptoRandomStream
   */
    abstract public void SetCryptoRandomStreamUC(int ucLength);

  /**
   * Sets an empty stream with the indicated length in unsigned shorts
   * 
   * @param      usLength  length in short integers (16 bits) of created CryptoRandomStream
   */
    abstract public void SetCryptoRandomStreamUS(int usLength);

  /**
   * Sets an empty stream with the indicated length in unsigned long int
   * 
   * @param      ulLength  length in long integers (32 bits) of created CryptoRandomStream
   */
    abstract public void SetCryptoRandomStreamUL(int ulLength);


  /**
   * Gets the buffer to the memory stream
   * 
   * @return    ByteBuffer: a pointer to CryptoRandomStream data 
   */
    public ByteBuffer GetCryptoRandomStreamMemory() {

        return this.cryptoBuffer;
    }

  /**
   * Sets the BaseCryptoRandomStream bit at current position and moves pointer to the following bit
   * 
   * @param     bit  byte representing a single bit, value 0 or 1
   */
    public void SetBitForward(byte bit) {

        this.SetBitPosition(this.position, bit);
        this.position++;
    }

  /**
   * Sets the BaseCryptoRandomStream bit at current position and moves pointer to the previous bit
   * 
   * @param     bit  byte representing a single bit, value 0 or 1
   */
    public void SetBitReverse(byte bit) {

        this.SetBitPosition(this.position, bit);
        this.position--;
    }

  /**
   * Gets the BaseCryptoRandomStream bit at current position and moves pointer to the following bit
   * 
   * @return    byte:  representing a single bit, value 0 or 1
   */
    public byte GetBitForward() {
        byte byteVAlue;

        byteVAlue = this.GetBitPosition(this.position);
        this.position++;
        return byteVAlue;
    }

  /**
   * Gets the BaseCryptoRandomStream bit at current position and moves pointer to the previous bit
   * 
   * @return    byte:  representing a single bit, value 0 or 1
   */
    public byte GetBitReverse() {
    	byte byteVAlue;

    	byteVAlue = this.GetBitPosition(this.position);
    	this.position--;
    	return byteVAlue;
    }

  /**
   * Sets the stream to an specified bit value (0 or 1)
   * 
   * @param     bit  byte representing a single bit, value 0 or 1
   */
    public void FillBit(byte bit) {
        byte byteValue;
      
        byteValue = (byte)0xff;
        if ( bit == 0) {
        	byteValue = 0x00; 
        }
        if ( bit == 1) {
        	byteValue = (byte)0xff; 
        }
        this.FillUC(byteValue);
    }

  /**
   * Sets the stream to an specified byte value
   * 
   * @param     uChar  byte value (8 bits)
   */
    public void FillUC(byte uChar) {
        
        for (int i = 0; i < this.cryptoBuffer.limit(); i++) {
        	this.cryptoBuffer.put(i, uChar);
        }
    }

  /**
   * Sets the stream to an specified short value
   * 
   * @param     uShort  short value (16 bits)
   */
    public void FillUS(short uShort) {
      
    	for (int i = 0; i < this.cryptoBuffer.limit()/TypeSizes.SHORT_BYTES; i++) {
    		this.cryptoBuffer.putShort(i * TypeSizes.SHORT_BYTES, uShort);
    	}
    }

  /**
   * Sets the stream to an specified bit unsigned long int value
   * 
   * @param     uLong  int value (32 bits)
   */
    public void FillUL(int uLong) {
      
    	for (int i = 0; i < this.cryptoBuffer.limit()/TypeSizes.INT_BYTES; i++) {
    		this.cryptoBuffer.putLong(i * TypeSizes.INT_BYTES, uLong);
    	}
    }

  /**
   * Sets the bit value (value 0 or 1) at specified postion, position based in array of bits
   * 
   * @param     position  position where to set the bit value
   * @param     bit       bit value, 0 or 1
   */
    public void SetBitPosition(int position, byte bit) {
        int bytePosition;
        int bitPosition;

        bytePosition = (int)Math.floor(position / TypeSizes.BYTE_BITS);
        bitPosition = position % TypeSizes.BYTE_BITS;
        this.cryptoBuffer.put(bytePosition, this.SetByteBitPosition(this.cryptoBuffer.get(bytePosition), bitPosition, bit));
    }

  /**
   * Sets the byte value at specified postion, position based in array of bytes
   * 
   * @param     position  position where to set the byte value
   * @param     uChar     byte value
   */
    public void SetUCPosition(int position, byte uChar) {
        
        this.cryptoBuffer.put(position, uChar);
    }

  /**
   * Sets the short value at specified postion, position based in array of shorts
   * 
   * @param     position  position where to set the short value
   * @param     uShort    short value
   */
    public void SetUSPosition(int position, short uShort) {
        
        this.cryptoBuffer.putShort(position * TypeSizes.SHORT_BYTES, uShort);
    }

  /**
   * Sets the int value at specified postion, position based in array of ints
   * 
   * @param     position  position where to set the int value
   * @param     uInt      int value
   */
    public void SetULPosition(int position, int uInt) {

      
        this.cryptoBuffer.putInt(position * TypeSizes.INT_BYTES, uInt);
    }

  /**
   * Sets the long value at specified postion, position based in array of longs
   * 
   * @param     position  position where to set the long value
   * @param     uLong     long value
   */
    public void Set64Position(int position, long uLong) {

      
        this.cryptoBuffer.putLong(position * TypeSizes.LONG_BYTES, uLong);
    }

  /**
   * Gets the bit value (value  0 or 1) as byte at specified postion, position based in array of bits
   * 
   * @param     position  bit position where to get the bit value
   * @return    byte:     bit value in a byte, value 0 or 1
   */
    public byte GetBitPosition(int position) {
        int bytePosition;
        int bitPosition;

        bytePosition = (int)Math.floor(position / TypeSizes.BYTE_BITS);
        bitPosition = position % TypeSizes.BYTE_BITS;
        return this.GetByteBitPosition(this.cryptoBuffer.get(bytePosition), bitPosition);
    }

  /**
   * Gets the byte at specified postion, position based in array of bytes
   * 
   * @param     position  byte position where to get the byte value
   * @return    byte:     byte value
   */
    public byte GetUCPosition(int position) {

        return this.cryptoBuffer.get(position);
    }

  /**
   * Gets the short value at specified postion, position based in array of shorts
   * 
   * @param     position  short position where to get the short value
   * @return    short:    short value
   */
    public short GetUSPosition(int position) {

        return this.cryptoBuffer.getShort(position * TypeSizes.SHORT_BYTES);
    }

  /**
   * Gets the int at specified postion, position based in array of ints
   * 
   * @param     position  int position where to get the int value
   * @return    int:      int value
   */
    public int GetULPosition(int position) {

        return this.cryptoBuffer.getInt(position * TypeSizes.INT_BYTES);
    }

  /**
   * Gets the long at specified postion, position based in array of longs
   * 
   * @param     position  int position where to get the long value
   * @return    long:     long value
   */
    public long Get64Position(int position) {

        return this.cryptoBuffer.getLong(position * TypeSizes.LONG_BYTES);
    }

  /**
   * Gets the baseCryptoRandomStream portion at specified position with 
   * from baseCryptoRandomStream object, position and length based in array of bytes
   * 
   * @param     subStream   CryptoRandomStream that will point the specified CryptoRandomStream portion 
   * @param     pos         position in bytes that will be the starting point of subStream CryptoRandomStream portion 
   * @throws    IndexOutOfBoundsException
   */
    abstract public void GetUCSubRandomStream(BaseCryptoRandomStream subStream, int pos) throws IndexOutOfBoundsException;
  
  /**
   * Gets the baseCryptoRandomStream portion at specified position
   * from baseCryptoRandomStream object, position and length based in array of shorts 
   * 
   * @param     subStream   CryptoRandomStream that will point the specified CryptoRandomStream portion 
   * @param     pos         position in shorts that will be the starting point of subStream CryptoRandomStream portion 
   * @throws    IndexOutOfBoundsException
   */
    abstract public void GetUSSubRandomStream(BaseCryptoRandomStream subStream, int pos) throws IndexOutOfBoundsException;
  
  /**
   * Gets the baseCryptoRandomStream portion at specified position
   * from baseCryptoRandomStream object, position and length based in array of ints
   * 
   * @param     subStream   CryptoRandomStream that will point the specified CryptoRandomStream portion 
   * @param     pos         position in ints that will be the starting point of subStream CryptoRandomStream portion 
   * @throws    IndexOutOfBoundsException
   */
    abstract public void GetULSubRandomStream(BaseCryptoRandomStream subStream, int pos) throws IndexOutOfBoundsException;
  
  /**
   * Gets the baseCryptoRandomStream portion at specified postion with a specified length 
   * from baseCryptoRandomStream object, position and length based in array of bytes
   * 
   * @param     subStream   CryptoRandomStream that will point the specified CryptoRandomStream portion 
   * @param     pos         position in bytes that will be the starting point of subStream CryptoRandomStream portion 
   * @param     length      length in bytes of subStream CryptoRandomStream portion 
   * @throws    IndexOutOfBoundsException
   */
    abstract public void GetUCSubRandomStream(BaseCryptoRandomStream subStream, int pos, int length) throws IndexOutOfBoundsException;
  
  /**
   * Gets the baseCryptoRandomStream portion at specified position with a specified length 
   * from baseCryptoRandomStream object, position and length based in array of shorts
   * 
   * @param     subStream   CryptoRandomStream that will point the specified CryptoRandomStream portion 
   * @param     pos         position in shorts that will be the starting point of subStream CryptoRandomStream portion 
   * @param     length      length in shorts of subStream CryptoRandomStream portion 
   * @throws    IndexOutOfBoundsException
   */
    abstract public void GetUSSubRandomStream(BaseCryptoRandomStream subStream, int pos, int length) throws IndexOutOfBoundsException;
  
  /**
   * Gets the baseCryptoRandomStream portion at specified position with a specified length 
   * from baseCryptoRandomStream object, position and length based in array of ints
   * 
   * @param     subStream   CryptoRandomStream that will point the specified CryptoRandomStream portion 
   * @param     pos         position in ints that will be the starting point of subStream CryptoRandomStream portion 
   * @param     length      length in ints of subStream CryptoRandomStream portion 
   * @throws    IndexOutOfBoundsException
   */
    abstract public void GetULSubRandomStream(BaseCryptoRandomStream subStream, int pos, int length) throws IndexOutOfBoundsException;
  
  /**
   * Reduces considered length of BaseCryptoRandomStream, real length is mantained,
   * but any access to BaseCryptoRandomStream through the interface will be limited to
   * the new considered length. BaseCryptoRandomStream will remain with its original
   * length and using the specified memory.
   * Reduces length in bits.
   * 
   * @param     value   number of bits of new length 
   */
    public void ReduceBitLength(int value) {

        if ( this.reducedBitLength != 0 ) {
        	this.reducedBitLength -= value; 
        }
        else {
        	this.reducedBitLength = this.bitLength - value;
        }
    }

  /**
   * Reduces considered length of BaseCryptoRandomStream, real length is mantained,
   * but any access to BaseCryptoRandomStream through the interface will be limited to
   * the new considered length. BaseCryptoRandomStream will remain with its original
   * length and using the specified memory.
   * Reduces length in unsigned chars.
   * 
   * @param     value   number of bytes of new length 
   */
    public void ReduceUCLength(int value) {

    	if ( this.reducedBitLength != 0 ) {
    		this.reducedBitLength -= (value * TypeSizes.BYTE_BITS); 
        }
        else {
        	this.reducedBitLength = this.bitLength - (value * TypeSizes.BYTE_BITS);
        }
    }

  /**
   * Reduces considered length of BaseCryptoRandomStream, real length is mantained,
   * but any access to BaseCryptoRandomStream through the interface will be limited to
   * the new considered length. BaseCryptoRandomStream will remain with its original
   * length and using the specified memory.
   * Reduces length in unsigned short ints.
   * 
   * @param     value   number of shorts of new length 
   */
    public void ReduceUSLength(int value) {

        if ( this.reducedBitLength != 0 ) {
        	this.reducedBitLength -= (value * TypeSizes.SHORT_BITS); 
        }
        else {
        	this.reducedBitLength = this.bitLength - (value * TypeSizes.SHORT_BITS);
        }
    }

  /**
   * Reduces considered length of BaseCryptoRandomStream, real length is mantained,
   * but any access to BaseCryptoRandomStream through the interface will be limited to
   * the new considered length. BaseCryptoRandomStream will remain with its original
   * length and using the specified memory.
   * Reduces length in unsigned long ints.
   * 
   * @param     value   number of ints of new length 
   */
    public void ReduceULLength(int value) {

        if ( this.reducedBitLength != 0 ) {
        	this.reducedBitLength -= (value * TypeSizes.LONG_BITS); 
        }
        else {
        	this.reducedBitLength = this.bitLength - (value * TypeSizes.LONG_BITS);
        }
    }

  /**
   * Indicates if BaseCryptoRandomStream's length has been reduced in a
   * fictitious way
   * 
   * @return    boolean: true if length stream has been reduced,  false if stream has its original length
   */
    public boolean ReducedLength() {

        return (this.reducedBitLength != 0);
    }

  /**
   * Restores original length of BaseCryptoRandomStream and removes any
   * fictitious reduced length
   * 
   */
    public void RestoreLength() {

        this.reducedBitLength = 0;
    }

  /**
   * Operator equal, compares the BaseCryptoRandomStream object with the
   * BaseCryptoRandomStream parameter
   * 
   * @param     otherStream    stream to be content compared with this stream
   */
    public boolean Equals(BaseCryptoRandomStream otherStream) {
    	ByteBuffer thisBuffer;
        int        thisLimit = 0;
        ByteBuffer paramBuffer;
        int        paramLimit = 0;
        int        result;

        if (otherStream == null) {
        	return false;
        }
        else {
        	if (this.GetBitLength() != otherStream.GetBitLength()) {
        		return false;
        	}
        	else {
        		thisBuffer = this.cryptoBuffer;
        		if ( this.ReducedLength() ) {
        			thisLimit = this.cryptoBuffer.limit();
        			thisBuffer.limit(this.GetUCLength());
        		}
        		paramBuffer = otherStream.cryptoBuffer;
        		if ( otherStream.ReducedLength() ) {
        			paramLimit = otherStream.cryptoBuffer.limit();
        			paramBuffer.limit(otherStream.GetUCLength());
        		}
        		result = thisBuffer.compareTo(paramBuffer);
        		if ( this.ReducedLength() ) {
        			this.cryptoBuffer.limit(thisLimit );
        		}
        		paramBuffer = otherStream.cryptoBuffer;
        		if ( otherStream.ReducedLength() ) {
        			otherStream.cryptoBuffer.limit(paramLimit);
        		}
        		return (result == 0);
        	}
        }
   }

  /**
   * Operator copy, copies the content of BaseCryptoRandomStream object
   * to the BaseCryptoRandomStream object passed as parameter
   * 
   * @param     target     target stream to copy the content of this stream
   * @throws    OutOfMemoryError
   */
    public void Copy(BaseCryptoRandomStream target) throws OutOfMemoryError {

        try {
        	if ( this.GetUCLength() != target.GetUCLength() )
        		throw new OutOfMemoryError("Streams have different lengths !");
        	else {
        		for (int i = 0; i < this.cryptoBuffer.limit(); i++) {
        			target.cryptoBuffer.put(i, this.cryptoBuffer.get(i));
        		}
        	}
        }
        catch (OutOfMemoryError e) {
        	throw e;
        }
    }

  /**
   * Gets the CryptoRandomStream type of the object
   * 
   * @return    CryptoRandomStreams:     the concrete type class of the stream
   */
    abstract public CryptoRandomStreams GetCryptoRandomStreamType();

}
 

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
 * Default type class of crypto random streams. Uses default Java memory management.
 * 
 * @author      Angel Ferré @ DiceLock Security
 * @version     5.0.0.1
 * @since       2011-09-28
 */
public class DefaultCryptoRandomStream extends BaseCryptoRandomStream {

  /**
   * Type of stream object
   */
    private static final CryptoRandomStreams cryptoRandomStreamType = CryptoRandomStreams.DefaultStream;

  /**
   * Pointer to base byte array of DefaultCryptoRandomStream data
   */
    public byte    baseCryptoStream[];
  /**
   * Starting point (byte) of this DefaultCryptoRandomStream inside baseCryptoStream
   */
    public int     baseOffset;
  /**
   * Boolean indicating if base byte array has been internally created
   *  true:     this object must release byte array instantiated when finalized
   *  false:    base byte array has been instantiated outside this object
   */
    public boolean autoMemory;

  /** 
   * Constructor, default
   */
    public DefaultCryptoRandomStream() {

    	super();
        this.baseCryptoStream = null;
        this.baseOffset = 0;
        this.autoMemory = false;
    }

  /**    
   * Constructor, creates an empty stream with the indicated bit length 
   * 
   * @param     streamLength    stream length in bits of new DefaultCryptoRandomStream
   * @throws    OutOfMemoryError
   */
    public DefaultCryptoRandomStream(int streamLength) throws OutOfMemoryError {
        
    	super();

    	int byteLength;
   
        if ( (streamLength % TypeSizes.BYTE_BITS) != 0 ) {
        	byteLength = streamLength/TypeSizes.BYTE_BITS + 1;
        }
        else {
        	byteLength = streamLength/TypeSizes.BYTE_BITS;
        }
        try {
        	this.baseCryptoStream = new byte[byteLength];
        	if (this.baseCryptoStream == null )
        		throw new OutOfMemoryError();
        	else {
        		this.bitLength = streamLength;
        		this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
        		this.baseOffset = 0;
        		this.autoMemory = true;
        	}
        }
        catch (OutOfMemoryError e) {
        	throw e;
        }
    }
   
  /**    
   * Constructor, sets the pointed stream of the indicated length in bits as DefaultCryptoRandomStream
   * 
   * @param     stream          assigns byte array as DefaultCryptoRandomStream data
   * @param     streamLength    stream length in bits of stream parameter
   */
    public DefaultCryptoRandomStream(byte[] stream, int streamLength) {
    	super();   

    	int byteLength;

    	if ( stream != null ) {
    		if ( (streamLength % TypeSizes.BYTE_BITS) != 0 ) {
    			byteLength = streamLength/TypeSizes.BYTE_BITS + 1;
    		}
    		else {
    			byteLength = streamLength/TypeSizes.BYTE_BITS;
    		}
    		this.baseCryptoStream = stream;
    		this.bitLength = streamLength;
    		this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    		this.position = 0;
    		this.baseOffset = 0;
    		this.autoMemory = false;
    	}
    }
   
  /**    
   * Destructor, zeroes all data
   */
    public void finalize() {
   
    	if ( this.autoMemory ) {
    		this.FillUC((byte)0x00); 
    	}
    	this.baseCryptoStream = null; 
    	this.bitLength = 0;
    	this.position = 0;
    	this.baseOffset = 0;
    	this.cryptoBuffer = null;
    	this.autoMemory = false;
    }
   
  /**    
   * Sets an empty stream with the indicated length in bits
   * 
   * @param       streamLength    stream length in bits of new DefaultCryptoRandomStream
   * @throws      OutOfMemoryError
   */
    public void SetCryptoRandomStreamBit(int streamLength) throws OutOfMemoryError {
    	int byteLength;
   
    	if (this.baseCryptoStream == null) {
    		if ( (streamLength % TypeSizes.BYTE_BITS) != 0 ) {
    			byteLength = streamLength/TypeSizes.BYTE_BITS + 1;
    		}
    		else {
    			byteLength = streamLength/TypeSizes.BYTE_BITS;
    		}
    		try {
    			this.baseCryptoStream = new byte[byteLength];
    			if (this.baseCryptoStream == null )
    				throw new OutOfMemoryError();
    			else {
    				this.bitLength = streamLength;
    				this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    				this.autoMemory = true;
    				this.position = 0;
    				this.baseOffset = 0;
    			}
    		}
    		catch (OutOfMemoryError e) {
    			throw e;
    		}
    	}
    }
   
  /**    
   * Sets the pointed stream of indicated length in bits
   * 
   * @param      stream          assigns byte array as DefaultCryptoRandomStream data
   * @param      streamLength    stream length in bits of stream parameter
   */
    public void SetCryptoRandomStreamBit(byte[] stream, int streamLength) {
    	int byteLength;
   
    	if (stream != null) {
    		if ( (streamLength % TypeSizes.BYTE_BITS) != 0 ) {
    			byteLength = streamLength/TypeSizes.BYTE_BITS + 1;
    		}
    		else {
    			byteLength = streamLength/TypeSizes.BYTE_BITS;
    		}
    		this.autoMemory = false;
    		this.bitLength = streamLength;
    		this.baseCryptoStream = stream;
    		this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    		this.position = 0;
    		this.baseOffset = 0;
    	}
    }
   
  /**    
   * Sets an empty stream with the indicated length in bytes
   * 
   * @param      streamLength    stream length in bytes of new DefaultCryptoRandomStream
   * @throws     OutOfMemoryError
   */
    public void SetCryptoRandomStreamUC(int streamLength) throws OutOfMemoryError {
   
    	if (this.baseCryptoStream == null) {
    		try {
    			this.baseCryptoStream = new byte[streamLength];
    			if (this.baseCryptoStream == null )
    				throw new OutOfMemoryError();
    			else {
    				this.autoMemory = true;
    				this.bitLength = streamLength * TypeSizes.BYTE_BITS;
    				this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, streamLength).slice();
    				this.position = 0;
    				this.baseOffset = 0;
    			}
    		}
    		catch (OutOfMemoryError e) {
    			throw e;
    		}
    	}
    }
   
  /**    
   * Sets the pointed stream of indicated length in bytes
   * 
   * @param     stream          assigns byte array as DefaultCryptoRandomStream data
   * @param     streamLength    stream length in bytes of stream parameter
   */
    public void SetCryptoRandomStreamUC(byte[] stream, int streamLength) {
   
    	if (stream != null) {
    		this.bitLength = streamLength * TypeSizes.BYTE_BITS;
    		this.baseCryptoStream = stream;
    		this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, streamLength).slice();
    		this.position = 0;
    		this.baseOffset = 0;
    		this.autoMemory = false;
    	}
    }
   
  /**    
   * Sets an empty stream with the indicated length in shorts
   * 
   * @param      streamLength    stream length in shorts of new DefaultCryptoRandomStream
   * @throws     OutOfMemoryError
   */
    public void SetCryptoRandomStreamUS(int streamLength) throws OutOfMemoryError {
    	int byteLength;
   
    	if (this.baseCryptoStream == null) {
    		byteLength = streamLength * TypeSizes.SHORT_BYTES;
    		try {
    			this.baseCryptoStream = new byte[byteLength];
    			if (this.baseCryptoStream == null )
    				throw new OutOfMemoryError();
    			else {
    				this.autoMemory = true;
    				this.bitLength = streamLength * TypeSizes.SHORT_BITS;
    				this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    				this.position = 0;
    				this.baseOffset = 0;
    			}
    		}
    		catch (OutOfMemoryError e) {
    			throw e;
    		}
    	}
    }
   
  /**    
   * Sets the pointed stream of indicated length in shorts
   * 
   * @param     stream          assigns byte array as DefaultCryptoRandomStream data
   * @param     streamLength    stream length in shorts of stream parameter
   */
    public void SetCryptoRandomStreamUS(byte[] stream, int streamLength) {
    	int byteLength;
   
    	if (stream != null) {
    		this.autoMemory = false;
    		byteLength = streamLength * TypeSizes.SHORT_BYTES;
    		this.bitLength = streamLength * TypeSizes.SHORT_BITS;
    		this.baseCryptoStream = stream;
    		this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    		this.position = 0;
    		this.baseOffset = 0;
    	}
    }
   
  /**    
   * Sets an empty stream with the indicated length in unsigned ints
   * 
   * @param      streamLength    stream length in ints of new DefaultCryptoRandomStream
   * @throws     OutOfMemoryError
   */
    public void SetCryptoRandomStreamUL(int streamLength) throws OutOfMemoryError {
    	int byteLength;
   
    	if (this.baseCryptoStream == null) {
    		byteLength = streamLength * TypeSizes.INT_BYTES;
    		try {
    			this.baseCryptoStream = new byte[byteLength];
    			if (this.baseCryptoStream == null )
    				throw new OutOfMemoryError();
    			else {
    				this.autoMemory = true;
    				this.bitLength = streamLength * TypeSizes.INT_BITS;
    				this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    				this.position = 0;
    				this.baseOffset = 0;
    			}
    		}
    		catch (OutOfMemoryError e) {
    			throw e;
    		}
    	}
    }
   
  /**    
   * Sets the pointed stream of indicated length in ints
   * 
   * @param     stream          assigns byte array as DefaultCryptoRandomStream data
   * @param     streamLength    stream length in ints of stream parameter
   */
    public void SetCryptoRandomStreamUL(byte[] stream, int streamLength) {
    	int byteLength;
   
    	if (stream != null) {
    		byteLength = streamLength * TypeSizes.INT_BYTES;
    		this.autoMemory = false;
    		this.bitLength = streamLength * TypeSizes.INT_BITS;
    		this.baseCryptoStream = stream;
    		this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, byteLength).slice();
    		this.position = 0;
    		this.baseOffset = 0;
    	}
    }
   
  /**    
   * Set the pointed stream as hexadecimal string
   * 
   * @param     hexStream       String parameter of hexadecimal data
   * @throws    OutOfMemoryError, DataFormatException
   */
    public void SetCryptoRandomStreamHexString(String hexStream) throws OutOfMemoryError, DataFormatException {
    	int  lengthUC, i, streamLength;
    	byte character;
   
    	try {
    		streamLength = hexStream.length();
    		if ( (streamLength % 2) != 0 ) {
    			throw new DataFormatException("Erroneous hexadecimal string!");
    		}
    		else {
    			lengthUC = streamLength / 2;
    			this.baseCryptoStream = new byte[lengthUC];
    			if (this.baseCryptoStream == null ) 
    				throw new OutOfMemoryError();
    			else {
    				this.autoMemory = true;
    				this.bitLength = lengthUC * TypeSizes.BYTE_BITS;
    				this.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, 0, lengthUC).slice();
    				this.position = 0;
    				this.baseOffset = 0;
    				for ( i = 0; i < lengthUC; i++ ) {
    					character = 0;
    					switch ( hexStream.charAt( 2 * i ) ) {
    						case '0' : character = 0x00; break;
    						case '1' : character = 0x10; break;
    						case '2' : character = 0x20; break;
    						case '3' : character = 0x30; break;
    						case '4' : character = 0x40; break;
    						case '5' : character = 0x50; break;
    						case '6' : character = 0x60; break;
    						case '7' : character = 0x70; break;
    						case '8' : character = (byte)0x80; break;
    						case '9' : character = (byte)0x90; break;
    						case 'A' : 
    						case 'a' : character = (byte)0xa0; break;
    						case 'B' : 
    						case 'b' : character = (byte)0xb0; break;
    						case 'C' : 
    						case 'c' : character = (byte)0xc0; break;
    						case 'D' : 
    						case 'd' : character = (byte)0xd0; break;
    						case 'E' : 
    						case 'e' : character = (byte)0xe0; break;
    						case 'F' : 
    						case 'f' : character = (byte)0xf0; break;
    						default : 
    							throw new DataFormatException("Erroneous hexadecimal string!");
    					}
    					switch ( hexStream.charAt( (2 * i) + 1 ) ) {
    						case '0' : character |= 0x00; break;
    						case '1' : character |= 0x01; break;
    						case '2' : character |= 0x02; break;
    						case '3' : character |= 0x03; break;
    						case '4' : character |= 0x04; break;
    						case '5' : character |= 0x05; break;
    						case '6' : character |= 0x06; break;
    						case '7' : character |= 0x07; break;
    						case '8' : character |= 0x08; break;
    						case '9' : character |= 0x09; break;
    						case 'A' : 
    						case 'a' : character |= 0x0a; break;
    						case 'B' : 
    						case 'b' : character |= 0x0b; break;
    						case 'C' : 
    						case 'c' : character |= 0x0c; break;
    						case 'D' : 
    						case 'd' : character |= 0x0d; break;
    						case 'E' : 
    						case 'e' : character |= 0x0e; break;
    						case 'F' : 
    						case 'f' : character |= 0x0f; break;
    						default : 
    							throw new DataFormatException("Erroneous hexadecimal string!");
    					}
    					this.SetUCPosition(i, character);
    				}
    			}
    		}
    	}
    	catch (OutOfMemoryError e) {
    		throw e;
    	}
    }

  /**    
   * Gets the baseCryptoRandomStream portion at specified position with 
   * from baseCryptoRandomStream object, position and length based in array of bytes
   * 
   * @param     subStream       DefaultCryptoRandomStream pointing a portion of this DefaultCryptoRandomStream  
   * @param     pos             starting position in bytes of this DefaultCryptoRandomStream for subStream, new 0 position of subStream
   * @throws    IndexOutOfBoundsException
   */
    public void GetUCSubRandomStream(BaseCryptoRandomStream subStream, int pos) throws IndexOutOfBoundsException {
    	int byteLength;

    	try {
    		if ( pos >= this.GetUCLength() ) {
    			throw new IndexOutOfBoundsException("Positions exceeded stream length !");
    		}
    		else {
    			subStream.bitLength = this.GetBitLength() - (pos * TypeSizes.BYTE_BITS);
    			if ( (subStream.bitLength % TypeSizes.BYTE_BITS) != 0 ) {
    				byteLength = subStream.bitLength/TypeSizes.BYTE_BITS + 1;
    			}
    			else {
    				byteLength = subStream.bitLength/TypeSizes.BYTE_BITS;
    			}
    			((DefaultCryptoRandomStream)subStream).baseCryptoStream = this.baseCryptoStream;
    			((DefaultCryptoRandomStream)subStream).baseOffset = this.baseOffset + pos;
    			subStream.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, ((DefaultCryptoRandomStream)subStream).baseOffset, byteLength).slice();
    			subStream.position = 0;
    			((DefaultCryptoRandomStream)subStream).autoMemory = false;
    		}
    	}
    	catch (IndexOutOfBoundsException e) {
    		throw e;
    	}
    }
  
  /**    
   * Gets the baseCryptoRandomStream portion at specified position
   * from baseCryptoRandomStream object, position and length based in array of shorts
   * 
   * @param     subStream       DefaultCryptoRandomStream pointing a portion of this DefaultCryptoRandomStream  
   * @param     pos             starting position in shorts of this DefaultCryptoRandomStream for subStream, new 0 position of subStream
   * @throws    IndexOutOfBoundsException
   */ 
    public void GetUSSubRandomStream(BaseCryptoRandomStream subStream, int pos) throws IndexOutOfBoundsException {
    	int byteLength;

    	try {
    		if ( pos >= this.GetUSLength() ) {
    			throw new IndexOutOfBoundsException("Positions exceeded stream length !");
    		}
    		else {
    			subStream.bitLength = this.GetBitLength() - (pos * TypeSizes.SHORT_BITS);
    			if ( (subStream.bitLength % TypeSizes.BYTE_BITS) != 0 ) {
    				byteLength = subStream.bitLength/TypeSizes.BYTE_BITS + 1;
    			}
    			else {
    				byteLength = subStream.bitLength/TypeSizes.BYTE_BITS;
    			}
    			((DefaultCryptoRandomStream)subStream).baseCryptoStream = this.baseCryptoStream;
    			((DefaultCryptoRandomStream)subStream).baseOffset = this.baseOffset + (pos * TypeSizes.SHORT_BYTES);
    			subStream.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, ((DefaultCryptoRandomStream)subStream).baseOffset, byteLength).slice();
    			subStream.position = 0;
    			((DefaultCryptoRandomStream)subStream).autoMemory = false;
    		}
    	}
    	catch (IndexOutOfBoundsException e) {
    		throw e;
    	}
    }
  
  /**    
   * Gets the baseCryptoRandomStream portion at specified position
   * from baseCryptoRandomStream object, position and length based in array of ints
   * 
   * @param     subStream       DefaultCryptoRandomStream pointing a portion of this DefaultCryptoRandomStream  
   * @param     pos             starting position in ints of this DefaultCryptoRandomStream for subStream, new 0 position of subStream
   * @throws    IndexOutOfBoundsException
   */ 
    public void GetULSubRandomStream(BaseCryptoRandomStream subStream, int pos) throws IndexOutOfBoundsException {
    	int byteLength;

    	try {
    		if ( pos >= this.GetULLength() ) {
    			throw new IndexOutOfBoundsException("Positions exceeded stream length !");
    		}
    		else {
    			subStream.bitLength = this.GetBitLength() - (pos * TypeSizes.INT_BITS);
    			if ( (subStream.bitLength % TypeSizes.BYTE_BITS) != 0 ) {
    				byteLength = subStream.bitLength/TypeSizes.BYTE_BITS + 1;
    			}
    			else {
    				byteLength = subStream.bitLength/TypeSizes.BYTE_BITS;
    			}
    			((DefaultCryptoRandomStream)subStream).baseCryptoStream = this.baseCryptoStream;
    			((DefaultCryptoRandomStream)subStream).baseOffset = this.baseOffset + (pos * TypeSizes.INT_BYTES);
    			subStream.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, ((DefaultCryptoRandomStream)subStream).baseOffset, byteLength).slice();
    			subStream.position = 0;
    			((DefaultCryptoRandomStream)subStream).autoMemory = false;
    		}
    	}
    	catch (IndexOutOfBoundsException e) {
    		throw e;
    	}
    }
  
  /**    
   * Gets the baseCryptoRandomStream portion at specified position with a specified length 
   * from baseCryptoRandomStream object, position and length based in array of bytes
   * 
   * @param     subStream       DefaultCryptoRandomStream pointing a portion of this DefaultCryptoRandomStream  
   * @param     pos             starting position in bytes of this DefaultCryptoRandomStream for subStream, new 0 position of subStream
   * @param     length          length in bytes of subStream
   */ 
    public void GetUCSubRandomStream(BaseCryptoRandomStream subStream, int pos, int length) throws IndexOutOfBoundsException {
    	int byteLength;

    	try {
    		if ( pos >= this.GetUCLength() ) {
    			throw new IndexOutOfBoundsException("Positions exceeded stream length !");
    		}
    		else {
    			if ( (pos + length) > (this.GetBitLength() / TypeSizes.BYTE_BITS) ) {
    				throw new IndexOutOfBoundsException("Positions plus length exceeded stream length !");
    			}
    			else {
    				subStream.bitLength = length * TypeSizes.BYTE_BITS;
    				if ( (subStream.bitLength % TypeSizes.BYTE_BITS) != 0 ) {
    					byteLength = subStream.bitLength/TypeSizes.BYTE_BITS + 1;
    				}
    				else {
    					byteLength = subStream.bitLength/TypeSizes.BYTE_BITS;
    				}
    				((DefaultCryptoRandomStream)subStream).baseCryptoStream = this.baseCryptoStream;
    				((DefaultCryptoRandomStream)subStream).baseOffset = this.baseOffset + pos;
    				subStream.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, ((DefaultCryptoRandomStream)subStream).baseOffset, byteLength).slice();
    				subStream.position = 0;
    				((DefaultCryptoRandomStream)subStream).autoMemory = false;
    			}
    		}
    	}
    	catch (IndexOutOfBoundsException e) {
    		throw e;
    	}
    }
  
  /**    
   * Gets the baseCryptoRandomStream portion at specified position with a specified length 
   *  from baseCryptoRandomStream object, position and length based in array of shorts
   * 
   * @param     subStream       DefaultCryptoRandomStream pointing a portion of this DefaultCryptoRandomStream  
   * @param     pos             starting position in shorts of this DefaultCryptoRandomStream for subStream, new 0 position of subStream
   * @param     length          length in shorts of subStream
   */ 
    public void GetUSSubRandomStream(BaseCryptoRandomStream subStream, int pos, int length) throws IndexOutOfBoundsException {
    	int byteLength;

    	try {
    		if ( pos >= this.GetUSLength() ) {
    			throw new IndexOutOfBoundsException("Positions exceeded stream length !");
    		}
    		else {
    			if ( (pos + length) > (this.GetBitLength() / TypeSizes.SHORT_BITS) ) {
    				throw new IndexOutOfBoundsException("Positions plus length exceeded stream length !");
    			}
    			else {
    				subStream.bitLength = length * TypeSizes.SHORT_BITS;
    				if ( (subStream.bitLength % TypeSizes.BYTE_BITS) != 0 ) {
    					byteLength = subStream.bitLength/TypeSizes.BYTE_BITS + 1;
    				}
    				else {
    					byteLength = subStream.bitLength/TypeSizes.BYTE_BITS;
    				}
    				((DefaultCryptoRandomStream)subStream).baseCryptoStream = this.baseCryptoStream;
    				((DefaultCryptoRandomStream)subStream).baseOffset = this.baseOffset + (pos * TypeSizes.SHORT_BYTES);
    				subStream.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, ((DefaultCryptoRandomStream)subStream).baseOffset, byteLength).slice();
    				subStream.position = 0;
    				((DefaultCryptoRandomStream)subStream).autoMemory = false;
    			}
    		}
    	}
    	catch (IndexOutOfBoundsException e) {
    		throw e;
    	}
    }
  
  /**    
   * Gets the baseCryptoRandomStream portion at specified position with a specified length 
   * from baseCryptoRandomStream object, position and length based in array of ints
   * 
   * @param     subStream       DefaultCryptoRandomStream pointing a portion of this DefaultCryptoRandomStream  
   * @param     pos             starting position in ints of this DefaultCryptoRandomStream for subStream, new 0 position of subStream
   * @param     length          length in ints of subStream
   */ 
    public void GetULSubRandomStream(BaseCryptoRandomStream subStream, int pos, int length) throws IndexOutOfBoundsException {
    	int byteLength;

    	try {
    		if ( pos >= this.GetULLength() ) {
    			throw new IndexOutOfBoundsException("Positions exceeded stream length !");
    		}
    		else {
    			if ( (pos + length) > (this.GetBitLength() / TypeSizes.INT_BITS) ) {
    				throw new IndexOutOfBoundsException("Positions plus length exceeded stream length !");
    			}
    			else {
    				subStream.bitLength = length * TypeSizes.INT_BITS;
    				if ( (subStream.bitLength % TypeSizes.BYTE_BITS) != 0 ) {
    					byteLength = subStream.bitLength/TypeSizes.BYTE_BITS + 1;
    				}
    				else {
    					byteLength = subStream.bitLength/TypeSizes.BYTE_BITS;
    				}
    				((DefaultCryptoRandomStream)subStream).baseCryptoStream = this.baseCryptoStream;
    				((DefaultCryptoRandomStream)subStream).baseOffset = this.baseOffset + (pos * TypeSizes.INT_BYTES);
    				subStream.cryptoBuffer = ByteBuffer.wrap(this.baseCryptoStream, ((DefaultCryptoRandomStream)subStream).baseOffset, byteLength).slice();
    				subStream.position = 0;
    				((DefaultCryptoRandomStream)subStream).autoMemory = false;
    			}
    		}
    	}
    	catch (IndexOutOfBoundsException e) {
    		throw e;
    	}
    }
   
  /**    
   * Gets the CryptoRandomStream type of the object
   * 
   * @return    CryptoRandomStreams:       object class type  
   */ 
    public CryptoRandomStreams GetCryptoRandomStreamType() {
   
      return DefaultCryptoRandomStream.cryptoRandomStreamType;
    }
}

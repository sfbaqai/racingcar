/* Generic definitions */




/* Assertions (useful to generate conditional code) */




/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2005-2008 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package it.unimi.dsi.fastutil.io;

import java.io.*;
import java.util.*;
import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.shorts.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.doubles.*;

/** Provides static methods to perform easily binary I/O.
 *
 * <P>This class fills some gaps in the Java API. First of all, you have two
 * buffered, easy-to-use methods to {@linkplain #storeObject(Object,CharSequence) store an object to a file}
 * or {@linkplain #loadObject(CharSequence) load an object from a file},
 * and two
 * buffered, easy-to-use methods to {@linkplain #storeObject(Object,OutputStream) store an object to an output stream}
 * or to {@linkplain #loadObject(InputStream) load an object from an input stream}.
 *
 * <p>Second, a natural operation on sequences of primitive elements is to load or
 * store them in binary form using the {@link DataInput} conventions.  This
 * method is much more flexible than storing arrays as objects, as it allows
 * for partial load, partial store, and makes it easy to read the
 * resulting files from other languages.
 * 
 * <P>For each primitive type, this class provides methods that read elements
 * from a {@link DataInput} or from a filename into an array. Analogously, there are
 * methods that store the content of an array (fragment) or the elements
 * returned by an iterator to a {@link DataOutput} or to a given filename. Files
 * are buffered using {@link FastBufferedInputStream} and {@link FastBufferedOutputStream}.
 *
 * <P>Since bytes can be read from or written to any stream, additional methods
 * makes it possible to {@linkplain #loadBytes(InputStream,byte[]) load bytes from} and
 * {@linkplain #storeBytes(byte[],OutputStream) store bytes to} a stream. Such methods
 * use the bulk-read methods of {@link InputStream} and {@link OutputStream}, but they
 * also include a workaround for <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6478546">bug #6478546</a>.
 *
 * <P>Finally, there are useful wrapper methods that {@linkplain #asIntIterator(CharSequence)
 * exhibit a file as a type-specific iterator}.
 * 
 * @since 4.4
 */

public class BinIO {

 private BinIO() {}

 /** Stores an object in a file given by a {@link File} object.
	 *
	 * @param o an object.
	 * @param file a file.
	 * @see #loadObject(File)
	 */

 public static void storeObject( final Object o, final File file ) throws IOException {
  final ObjectOutputStream oos = new ObjectOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
  oos.writeObject( o );
  oos.close();
 }

 /** Stores an object in a file given by a pathname.
	 *
	 * @param o an object.
	 * @param filename a filename.
	 * @see #loadObject(CharSequence)
	 */

 public static void storeObject( final Object o, final CharSequence filename ) throws IOException {
  storeObject( o, new File( filename.toString() ) );
 }

 /** Loads an object from a file given by a {@link File} object.
	 *
	 * @param file a file.
	 * @return the object stored under the given file.
	 * @see #storeObject(Object, File)
	 */
 public static Object loadObject( final File file ) throws IOException, ClassNotFoundException {
  final ObjectInputStream ois = new ObjectInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) );
  final Object result = ois.readObject();
  ois.close();
  return result;
 }

 /** Loads an object from a file given by a pathname.
	 *
	 * @param filename a filename.
	 * @return the object stored under the given filename.
	 * @see #storeObject(Object, CharSequence)
	 */
 public static Object loadObject( final CharSequence filename ) throws IOException, ClassNotFoundException {
  return loadObject( new File( filename.toString() ) );
 }

 /** Stores an object in a given output stream.
	 *
	 * This methods buffers <code>s</code>, and flushes all wrappers after
	 * calling <code>writeObject()</code>, but does not close <code>s</code>.
	 *
	 * @param o an object.
	 * @param s an output stream.
	 * @see #loadObject(InputStream)
	 */

 public static void storeObject( final Object o, final OutputStream s ) throws IOException {
  final ObjectOutputStream oos = new ObjectOutputStream( new FastBufferedOutputStream( s ) );
  oos.writeObject( o );
  oos.flush();
 }

 /** Loads an object from a given input stream.
	 *
	 * <p><STRONG>Warning</STRONG>: this method buffers the input stream. As a consequence,
	 * subsequent reads from the same stream may not give the desired results, as bytes
	 * may have been read by the internal buffer, but not used by <code>readObject()</code>.
	 * This is a feature, as this method is targeted at one-shot reading from streams,
	 * e.g., reading exactly one object from {@link System#in}.
	 *
	 * @param s an input stream.
	 * @return the object read from the given input stream.
	 * @see #storeObject(Object, OutputStream)
	 */
 public static Object loadObject( final InputStream s ) throws IOException, ClassNotFoundException {
  final ObjectInputStream ois = new ObjectInputStream( new FastBufferedInputStream( s ) );
  final Object result = ois.readObject();
  return result;
 }





/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadBooleans( final DataInput dataInput, final boolean[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.booleans.BooleanArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readBoolean();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadBooleans( final DataInput dataInput, final boolean[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readBoolean();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadBooleans( final File file, final boolean[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.booleans.BooleanArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readBoolean();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadBooleans( final CharSequence filename, final boolean[] array, final int offset, final int length ) throws IOException {
 return loadBooleans( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadBooleans( final File file, final boolean[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readBoolean();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadBooleans( final CharSequence filename, final boolean[] array ) throws IOException {
 return loadBooleans( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static boolean[] loadBooleans( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );


 final long length = fis.getChannel().size();




 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final boolean[] array = new boolean[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readBoolean();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static boolean[] loadBooleans( final CharSequence filename ) throws IOException {
 return loadBooleans( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeBooleans( final boolean array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.booleans.BooleanArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeBoolean( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeBooleans( final boolean array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeBoolean( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeBooleans( final boolean array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.booleans.BooleanArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeBoolean( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeBooleans( final boolean array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeBooleans( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeBooleans( final boolean array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeBoolean( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeBooleans( final boolean array[], final CharSequence filename ) throws IOException {
 storeBooleans( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeBooleans( final BooleanIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeBoolean( i.nextBoolean() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeBooleans( final BooleanIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeBoolean( i.nextBoolean() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeBooleans( final BooleanIterator i, final CharSequence filename ) throws IOException {
 storeBooleans( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class BooleanDataInputWrapper extends AbstractBooleanIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private boolean next;

 public BooleanDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readBoolean();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public boolean nextBoolean() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static BooleanIterator asBooleanIterator( final DataInput dataInput ) {
 return new BooleanDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static BooleanIterator asBooleanIterator( final File file ) throws IOException {
 return new BooleanDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static BooleanIterator asBooleanIterator( final CharSequence filename ) throws IOException {
 return asBooleanIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */




// HORRIBLE kluges to work around bug #6478546

private final static int MAX_IO_LENGTH = 1024 * 1024;

private static int read( final InputStream is, final byte a[], final int offset, final int length ) throws IOException {
 if ( length == 0 ) return 0;

 int read = 0, result;
 do {
  result = is.read( a, offset + read, Math.min( length - read, MAX_IO_LENGTH ) );
  if ( result < 0 ) return read;
  read += result;
 } while( read < length );

 return read;
}

private static void write( final OutputStream outputStream, final byte a[], final int offset, final int length ) throws IOException {
 int written = 0;
 while( written < length ) {
  outputStream.write( a, offset + written, Math.min( length - written, MAX_IO_LENGTH ) );
  written += Math.min( length - written, MAX_IO_LENGTH );
 }
}

private static void write( final DataOutput dataOutput, final byte a[], final int offset, final int length ) throws IOException {
 int written = 0;
 while( written < length ) {
  dataOutput.write( a, offset + written, Math.min( length - written, MAX_IO_LENGTH ) );
  written += Math.min( length - written, MAX_IO_LENGTH );
 }
}

// Additional read/write methods to work around the DataInput/DataOutput schizophrenia.

/** Loads bytes from a given input stream, storing them in a given array fragment.
 *
 * <p>Note that this method is going to be significantly faster than {@link #loadBytes(DataInput,byte[],int,int)}
 * as it uses {@link InputStream}'s bulk-read methods.
 *
 * @param inputStream an input stream.
 * @param array an array which will be filled with data from <code>inputStream</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>inputStream</code> (it might be less than <code>length</code> if <code>inputStream</code> ends).
 */
public static int loadBytes( final InputStream inputStream, final byte[] array, final int offset, final int length ) throws IOException {
 return read( inputStream, array, offset, length );
}

/** Loads bytes from a given input stream, storing them in a given array.
 *
 * <p>Note that this method is going to be significantly faster than {@link #loadBytes(DataInput,byte[])}
 * as it uses {@link InputStream}'s bulk-read methods.
 *
 * @param inputStream an input stream.
 * @param array an array which will be filled with data from <code>inputStream</code>.
 * @return the number of elements actually read from <code>inputStream</code> (it might be less than the array length if <code>inputStream</code> ends).
 */
public static int loadBytes( final InputStream inputStream, final byte[] array ) throws IOException {
 return read( inputStream, array, 0, array.length );
}

/** Stores an array fragment to a given output stream.
 *
 * <p>Note that this method is going to be significantly faster than {@link #storeBytes(byte[],int,int,DataOutput)}
 * as it uses {@link OutputStream}'s bulk-read methods.
 *
 * @param array an array whose elements will be written to <code>outputStream</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param outputStream an output stream.
 */
public static void storeBytes( final byte array[], final int offset, final int length, final OutputStream outputStream ) throws IOException {
 write( outputStream, array, offset, length );
}

/** Stores an array to a given output stream.
 *
 * <p>Note that this method is going to be significantly faster than {@link #storeBytes(byte[],DataOutput)}
 * as it uses {@link OutputStream}'s bulk-read methods.
 *
 * @param array an array whose elements will be written to <code>outputStream</code>.
 * @param outputStream an output stream.
 */
public static void storeBytes( final byte array[], final OutputStream outputStream ) throws IOException {
 write( outputStream, array, 0, array.length );
}




/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadBytes( final DataInput dataInput, final byte[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.bytes.ByteArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readByte();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadBytes( final DataInput dataInput, final byte[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readByte();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadBytes( final File file, final byte[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.bytes.ByteArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );

 final int result = read( fis, array, offset, length );
 fis.close();
 return result;
}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadBytes( final CharSequence filename, final byte[] array, final int offset, final int length ) throws IOException {
 return loadBytes( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadBytes( final File file, final byte[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );

 final int result = read( fis, array, 0, array.length );
 fis.close();
 return result;
}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadBytes( final CharSequence filename, final byte[] array ) throws IOException {
 return loadBytes( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static byte[] loadBytes( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Byte.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final byte[] array = new byte[ (int)length ];


 if ( read( fis, array, 0, (int)length ) < length ) throw new EOFException();
 fis.close();





 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static byte[] loadBytes( final CharSequence filename ) throws IOException {
 return loadBytes( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeBytes( final byte array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.bytes.ByteArrays.ensureOffsetLength( array, offset, length );

 write( dataOutput, array, offset, length );



}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeBytes( final byte array[], final DataOutput dataOutput ) throws IOException {

 write( dataOutput, array, 0, array.length );




}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeBytes( final byte array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.bytes.ByteArrays.ensureOffsetLength( array, offset, length );

 final OutputStream os = new FastBufferedOutputStream( new FileOutputStream( file ) );
 write( os, array, offset, length );
 os.close();





}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeBytes( final byte array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeBytes( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeBytes( final byte array[], final File file ) throws IOException {

 final OutputStream os = new FastBufferedOutputStream( new FileOutputStream( file ) );
 write( os, array, 0, array.length );
 os.close();






}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeBytes( final byte array[], final CharSequence filename ) throws IOException {
 storeBytes( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeBytes( final ByteIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeByte( i.nextByte() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeBytes( final ByteIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeByte( i.nextByte() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeBytes( final ByteIterator i, final CharSequence filename ) throws IOException {
 storeBytes( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class ByteDataInputWrapper extends AbstractByteIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private byte next;

 public ByteDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readByte();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public byte nextByte() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static ByteIterator asByteIterator( final DataInput dataInput ) {
 return new ByteDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static ByteIterator asByteIterator( final File file ) throws IOException {
 return new ByteDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static ByteIterator asByteIterator( final CharSequence filename ) throws IOException {
 return asByteIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadShorts( final DataInput dataInput, final short[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.shorts.ShortArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readShort();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadShorts( final DataInput dataInput, final short[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readShort();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadShorts( final File file, final short[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.shorts.ShortArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readShort();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadShorts( final CharSequence filename, final short[] array, final int offset, final int length ) throws IOException {
 return loadShorts( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadShorts( final File file, final short[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readShort();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadShorts( final CharSequence filename, final short[] array ) throws IOException {
 return loadShorts( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static short[] loadShorts( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Short.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final short[] array = new short[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readShort();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static short[] loadShorts( final CharSequence filename ) throws IOException {
 return loadShorts( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeShorts( final short array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.shorts.ShortArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeShort( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeShorts( final short array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeShort( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeShorts( final short array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.shorts.ShortArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeShort( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeShorts( final short array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeShorts( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeShorts( final short array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeShort( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeShorts( final short array[], final CharSequence filename ) throws IOException {
 storeShorts( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeShorts( final ShortIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeShort( i.nextShort() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeShorts( final ShortIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeShort( i.nextShort() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeShorts( final ShortIterator i, final CharSequence filename ) throws IOException {
 storeShorts( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class ShortDataInputWrapper extends AbstractShortIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private short next;

 public ShortDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readShort();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public short nextShort() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static ShortIterator asShortIterator( final DataInput dataInput ) {
 return new ShortDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static ShortIterator asShortIterator( final File file ) throws IOException {
 return new ShortDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static ShortIterator asShortIterator( final CharSequence filename ) throws IOException {
 return asShortIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadChars( final DataInput dataInput, final char[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.chars.CharArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readChar();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadChars( final DataInput dataInput, final char[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readChar();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadChars( final File file, final char[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.chars.CharArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readChar();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadChars( final CharSequence filename, final char[] array, final int offset, final int length ) throws IOException {
 return loadChars( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadChars( final File file, final char[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readChar();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadChars( final CharSequence filename, final char[] array ) throws IOException {
 return loadChars( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static char[] loadChars( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Character.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final char[] array = new char[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readChar();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static char[] loadChars( final CharSequence filename ) throws IOException {
 return loadChars( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeChars( final char array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.chars.CharArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeChar( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeChars( final char array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeChar( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeChars( final char array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.chars.CharArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeChar( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeChars( final char array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeChars( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeChars( final char array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeChar( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeChars( final char array[], final CharSequence filename ) throws IOException {
 storeChars( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeChars( final CharIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeChar( i.nextChar() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeChars( final CharIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeChar( i.nextChar() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeChars( final CharIterator i, final CharSequence filename ) throws IOException {
 storeChars( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class CharDataInputWrapper extends AbstractCharIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private char next;

 public CharDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readChar();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public char nextChar() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static CharIterator asCharIterator( final DataInput dataInput ) {
 return new CharDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static CharIterator asCharIterator( final File file ) throws IOException {
 return new CharDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static CharIterator asCharIterator( final CharSequence filename ) throws IOException {
 return asCharIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadInts( final DataInput dataInput, final int[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.ints.IntArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readInt();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadInts( final DataInput dataInput, final int[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readInt();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadInts( final File file, final int[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.ints.IntArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readInt();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadInts( final CharSequence filename, final int[] array, final int offset, final int length ) throws IOException {
 return loadInts( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadInts( final File file, final int[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readInt();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadInts( final CharSequence filename, final int[] array ) throws IOException {
 return loadInts( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static int[] loadInts( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Integer.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final int[] array = new int[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readInt();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static int[] loadInts( final CharSequence filename ) throws IOException {
 return loadInts( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeInts( final int array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.ints.IntArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeInt( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeInts( final int array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeInt( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeInts( final int array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.ints.IntArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeInt( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeInts( final int array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeInts( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeInts( final int array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeInt( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeInts( final int array[], final CharSequence filename ) throws IOException {
 storeInts( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeInts( final IntIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeInt( i.nextInt() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeInts( final IntIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeInt( i.nextInt() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeInts( final IntIterator i, final CharSequence filename ) throws IOException {
 storeInts( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class IntDataInputWrapper extends AbstractIntIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private int next;

 public IntDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readInt();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public int nextInt() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static IntIterator asIntIterator( final DataInput dataInput ) {
 return new IntDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static IntIterator asIntIterator( final File file ) throws IOException {
 return new IntDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static IntIterator asIntIterator( final CharSequence filename ) throws IOException {
 return asIntIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadLongs( final DataInput dataInput, final long[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.longs.LongArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readLong();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadLongs( final DataInput dataInput, final long[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readLong();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadLongs( final File file, final long[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.longs.LongArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readLong();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadLongs( final CharSequence filename, final long[] array, final int offset, final int length ) throws IOException {
 return loadLongs( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadLongs( final File file, final long[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readLong();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadLongs( final CharSequence filename, final long[] array ) throws IOException {
 return loadLongs( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static long[] loadLongs( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Long.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final long[] array = new long[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readLong();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static long[] loadLongs( final CharSequence filename ) throws IOException {
 return loadLongs( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeLongs( final long array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.longs.LongArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeLong( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeLongs( final long array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeLong( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeLongs( final long array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.longs.LongArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeLong( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeLongs( final long array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeLongs( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeLongs( final long array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeLong( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeLongs( final long array[], final CharSequence filename ) throws IOException {
 storeLongs( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeLongs( final LongIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeLong( i.nextLong() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeLongs( final LongIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeLong( i.nextLong() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeLongs( final LongIterator i, final CharSequence filename ) throws IOException {
 storeLongs( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class LongDataInputWrapper extends AbstractLongIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private long next;

 public LongDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readLong();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public long nextLong() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static LongIterator asLongIterator( final DataInput dataInput ) {
 return new LongDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static LongIterator asLongIterator( final File file ) throws IOException {
 return new LongDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static LongIterator asLongIterator( final CharSequence filename ) throws IOException {
 return asLongIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadFloats( final DataInput dataInput, final float[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.floats.FloatArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readFloat();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadFloats( final DataInput dataInput, final float[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readFloat();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadFloats( final File file, final float[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.floats.FloatArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readFloat();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadFloats( final CharSequence filename, final float[] array, final int offset, final int length ) throws IOException {
 return loadFloats( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadFloats( final File file, final float[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readFloat();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadFloats( final CharSequence filename, final float[] array ) throws IOException {
 return loadFloats( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static float[] loadFloats( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Float.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final float[] array = new float[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readFloat();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static float[] loadFloats( final CharSequence filename ) throws IOException {
 return loadFloats( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeFloats( final float array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.floats.FloatArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeFloat( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeFloats( final float array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeFloat( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeFloats( final float array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.floats.FloatArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeFloat( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeFloats( final float array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeFloats( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeFloats( final float array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeFloat( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeFloats( final float array[], final CharSequence filename ) throws IOException {
 storeFloats( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeFloats( final FloatIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeFloat( i.nextFloat() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeFloats( final FloatIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeFloat( i.nextFloat() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeFloats( final FloatIterator i, final CharSequence filename ) throws IOException {
 storeFloats( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class FloatDataInputWrapper extends AbstractFloatIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private float next;

 public FloatDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readFloat();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public float nextFloat() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static FloatIterator asFloatIterator( final DataInput dataInput ) {
 return new FloatDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static FloatIterator asFloatIterator( final File file ) throws IOException {
 return new FloatDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static FloatIterator asFloatIterator( final CharSequence filename ) throws IOException {
 return asFloatIterator( new File( filename.toString() ) );
}



/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2004-2008 Sebastiano Vigna 
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
/** Loads elements from a given data input, storing them in a given array fragment.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than <code>length</code> if <code>dataInput</code> ends).
 */
public static int loadDoubles( final DataInput dataInput, final double[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.doubles.DoubleArrays.ensureOffsetLength( array, offset, length );
 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dataInput.readDouble();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a given data input, storing them in a given array.
 *
 * @param dataInput a data input.
 * @param array an array which will be filled with data from <code>dataInput</code>.
 * @return the number of elements actually read from <code>dataInput</code> (it might be less than the array length if <code>dataInput</code> ends).
 */
public static int loadDoubles( final DataInput dataInput, final double[] array ) throws IOException {
 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dataInput.readDouble();
 }
 catch( EOFException itsOk ) {}
 return i;
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array fragment.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadDoubles( final File file, final double[] array, final int offset, final int length ) throws IOException {
 it.unimi.dsi.fastutil.doubles.DoubleArrays.ensureOffsetLength( array, offset, length );

 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  for( i = 0; i < length; i++ ) array[ i + offset ] = dis.readDouble();
 }
 catch( EOFException itsOk ) {}

 dis.close();
 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array fragment.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @param offset the index of the first element of <code>array</code> to be filled.
 * @param length the number of elements of <code>array</code> to be filled.
 * @return the number of elements actually read from the given file (it might be less than <code>length</code> if the file is too short).
 */
public static int loadDoubles( final CharSequence filename, final double[] array, final int offset, final int length ) throws IOException {
 return loadDoubles( new File( filename.toString() ), array, offset, length );
}

/** Loads elements from a file given by a {@link File} object, storing them in a given array.
 *
 * @param file a file.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadDoubles( final File file, final double[] array ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );

 int i = 0;
 try {
  final int length = array.length;
  for( i = 0; i < length; i++ ) array[ i ] = dis.readDouble();
 }
 catch( EOFException itsOk ) {}

 dis.close();

 return i;

}

/** Loads elements from a file given by a pathname, storing them in a given array.
 *
 * @param filename a filename.
 * @param array an array which will be filled with data from the specified file.
 * @return the number of elements actually read from the given file (it might be less than the array length if the file is too short).
 */
public static int loadDoubles( final CharSequence filename, final double[] array ) throws IOException {
 return loadDoubles( new File( filename.toString() ), array );
}

/** Loads elements from a file given by a {@link File} object, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param file a file.
 * @return an array filled with the content of the specified file.
 */
public static double[] loadDoubles( final File file ) throws IOException {
 final FileInputStream fis = new FileInputStream( file );




 final long length = fis.getChannel().size() / ( Double.SIZE / 8 );


 if ( length > Integer.MAX_VALUE ) throw new IllegalArgumentException( "File too long: " + fis.getChannel().size()+ " bytes (" + length + " elements)" );

 final double[] array = new double[ (int)length ];





 final DataInputStream dis = new DataInputStream( new FastBufferedInputStream( fis ) );
 for( int i = 0; i < length; i++ ) array[ i ] = dis.readDouble();
 dis.close();

 return array;
}

/** Loads elements from a file given by a filename, storing them in a new array.
 *
 * <P>Note that the length of the returned array will be computed 
 * dividing the specified file size by the number of bytes used to
 * represent each element.
 *
 * @param filename a filename.
 * @return an array filled with the content of the specified file.
 */
public static double[] loadDoubles( final CharSequence filename ) throws IOException {
 return loadDoubles( new File( filename.toString() ) );
}

/** Stores an array fragment to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param dataOutput a data output.
 */
public static void storeDoubles( final double array[], final int offset, final int length, final DataOutput dataOutput ) throws IOException {
 it.unimi.dsi.fastutil.doubles.DoubleArrays.ensureOffsetLength( array, offset, length );



 for( int i = 0; i < length; i++ ) dataOutput.writeDouble( array[ offset + i ] );

}

/** Stores an array to a given data output.
 *
 * @param array an array whose elements will be written to <code>dataOutput</code>.
 * @param dataOutput a data output.
 */
public static void storeDoubles( final double array[], final DataOutput dataOutput ) throws IOException {



 final int length = array.length;
 for( int i = 0; i < length; i++ ) dataOutput.writeDouble( array[ i ] );

}

/** Stores an array fragment to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param file a file.
 */
public static void storeDoubles( final double array[], final int offset, final int length, final File file ) throws IOException {
 it.unimi.dsi.fastutil.doubles.DoubleArrays.ensureOffsetLength( array, offset, length );





 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeDouble( array[ offset + i ] );
 dos.close();

}

/** Stores an array fragment to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param offset the index of the first element of <code>array</code> to be written.
 * @param length the number of elements of <code>array</code> to be written.
 * @param filename a filename.
 */
public static void storeDoubles( final double array[], final int offset, final int length, final CharSequence filename ) throws IOException {
 storeDoubles( array, offset, length, new File( filename.toString() ) );
}

/** Stores an array to a file given by a {@link File} object.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeDoubles( final double array[], final File file ) throws IOException {





 final int length = array.length;
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 for( int i = 0; i < length; i++ ) dos.writeDouble( array[ i ] );
 dos.close();

}

/** Stores an array to a file given by a pathname.
 *
 * @param array an array whose elements will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeDoubles( final double array[], final CharSequence filename ) throws IOException {
 storeDoubles( array, new File( filename.toString() ) );
}

/** Stores the element returned by an iterator to a given data output.
 *
 * @param i an iterator whose output will be written to <code>dataOutput</code>.
 * @param dataOutput a filename.
 */
public static void storeDoubles( final DoubleIterator i, final DataOutput dataOutput ) throws IOException {
 while( i.hasNext() ) dataOutput.writeDouble( i.nextDouble() );
}

/** Stores the element returned by an iterator to a file given by a {@link File} object.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param file a file.
 */
public static void storeDoubles( final DoubleIterator i, final File file ) throws IOException {
 final DataOutputStream dos = new DataOutputStream( new FastBufferedOutputStream( new FileOutputStream( file ) ) );
 while( i.hasNext() ) dos.writeDouble( i.nextDouble() );
 dos.close();
}

/** Stores the element returned by an iterator to a file given by a pathname.
 *
 * @param i an iterator whose output will be written to <code>filename</code>.
 * @param filename a filename.
 */
public static void storeDoubles( final DoubleIterator i, final CharSequence filename ) throws IOException {
 storeDoubles( i, new File( filename.toString() ) );
}

/** A wrapper that exhibits the content of a data input stream as a type-specific iterator. */

final private static class DoubleDataInputWrapper extends AbstractDoubleIterator {
 final private DataInput dataInput;
 private boolean toAdvance = true;
 private boolean endOfProcess = false;
 private double next;

 public DoubleDataInputWrapper( final DataInput dataInput ) {
  this.dataInput = dataInput;
 }

 public boolean hasNext() {
  if ( ! toAdvance ) return ! endOfProcess;

  toAdvance = false;

  try {
   next = dataInput.readDouble();
  }
  catch( EOFException eof ) {
   endOfProcess = true;
  }
  catch( IOException rethrow ) { throw new RuntimeException( rethrow ); }

  return ! endOfProcess;
 }

 public double nextDouble() {
  if (! hasNext()) throw new NoSuchElementException();
  toAdvance = true;
  return next;
 }
}



/** Wraps the given data input stream into an iterator.
 *
 * @param dataInput a data input.
 */
public static DoubleIterator asDoubleIterator( final DataInput dataInput ) {
 return new DoubleDataInputWrapper( dataInput );
}

/** Wraps a file given by a {@link File} object into an iterator.
 *
 * @param file a file.
 */
public static DoubleIterator asDoubleIterator( final File file ) throws IOException {
 return new DoubleDataInputWrapper( new DataInputStream( new FastBufferedInputStream( new FileInputStream( file ) ) ) );
}

/** Wraps a file given by a pathname into an iterator.
 *
 * @param filename a filename.
 */
public static DoubleIterator asDoubleIterator( final CharSequence filename ) throws IOException {
 return asDoubleIterator( new File( filename.toString() ) );
}



}

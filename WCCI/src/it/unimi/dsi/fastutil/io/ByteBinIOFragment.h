/* Generic definitions */


#define PACKAGE it.unimi.dsi.fastutil.bytes
#define VALUE_PACKAGE it.unimi.dsi.fastutil.objects
/* Assertions (useful to generate conditional code) */
#unassert keyclass
#assert keyclass(Byte)
#unassert keys
 #assert keys(primitive)
#unassert valueclass
#assert valueclass(Object)
#unassert values
 #assert values(reference)
/* Current type and class (and size, if applicable) */
#define KEY_TYPE byte
#define VALUE_TYPE Object
#define KEY_CLASS Byte
#define VALUE_CLASS Object
#if #keyclass(Object) || #keyclass(Reference)
#define KEY_GENERIC_CLASS K
#define KEY_GENERIC_TYPE K
#define KEY_GENERIC <K>
#define KEY_GENERIC_WILDCARD <?>
#define KEY_EXTENDS_GENERIC <? extends K>
#define KEY_SUPER_GENERIC <? super K>
#define KEY_GENERIC_CAST (K)
#define KEY_GENERIC_ARRAY_CAST (K[])
#else
#define KEY_GENERIC_CLASS KEY_CLASS
#define KEY_GENERIC_TYPE KEY_TYPE
#define KEY_GENERIC
#define KEY_GENERIC_WILDCARD
#define KEY_EXTENDS_GENERIC
#define KEY_SUPER_GENERIC
#define KEY_GENERIC_CAST
#define KEY_GENERIC_ARRAY_CAST
#endif
#if #valueclass(Object) || #valueclass(Reference)
#define VALUE_GENERIC_CLASS V
#define VALUE_GENERIC_TYPE V
#define VALUE_GENERIC <V>
#define VALUE_EXTENDS_GENERIC <? extends V>
#define VALUE_GENERIC_CAST (V)
#define VALUE_GENERIC_ARRAY_CAST (V[])
#else
#define VALUE_GENERIC_CLASS VALUE_CLASS
#define VALUE_GENERIC_TYPE VALUE_TYPE
#define VALUE_GENERIC
#define VALUE_EXTENDS_GENERIC
#define VALUE_GENERIC_CAST
#define VALUE_GENERIC_ARRAY_CAST
#endif
#if #keyclass(Object) || #keyclass(Reference)
#if #valueclass(Object) || #valueclass(Reference)
#define KEY_VALUE_GENERIC <K,V>
#define KEY_VALUE_EXTENDS_GENERIC <? extends K, ? extends V>
#else
#define KEY_VALUE_GENERIC <K>
#define KEY_VALUE_EXTENDS_GENERIC <? extends K>
#endif
#else
#if #valueclass(Object) || #valueclass(Reference)
#define KEY_VALUE_GENERIC <V>
#define KEY_VALUE_EXTENDS_GENERIC <? extends V>
#else
#define KEY_VALUE_GENERIC
#define KEY_VALUE_EXTENDS_GENERIC
#endif
#endif
/* Value methods */
#define KEY_VALUE byteValue
#define VALUE_VALUE ObjectValue
/* Interfaces (keys) */
#define COLLECTION ByteCollection

#define SET ByteSet

#define SORTED_SET ByteSortedSet

#define STD_SORTED_SET ByteSortedSet

#define FUNCTION Byte2ObjectFunction
#define MAP Byte2ObjectMap
#define SORTED_MAP Byte2ObjectSortedMap
#if #keyclass(Object) || #keyclass(Reference)
#define STD_SORTED_MAP SortedMap

#else
#define STD_SORTED_MAP Byte2ObjectSortedMap

#endif
#define LIST ByteList

#define STACK ByteStack

#define PRIORITY_QUEUE BytePriorityQueue

#define INDIRECT_PRIORITY_QUEUE ByteIndirectPriorityQueue

#define INDIRECT_DOUBLE_PRIORITY_QUEUE ByteIndirectDoublePriorityQueue

#define KEY_ITERATOR ByteIterator

#define KEY_ITERABLE ByteIterable

#define KEY_BIDI_ITERATOR ByteBidirectionalIterator

#define KEY_LIST_ITERATOR ByteListIterator

#define STD_KEY_ITERATOR ByteIterator

#define KEY_COMPARATOR ByteComparator

/* Interfaces (values) */
#define VALUE_COLLECTION ObjectCollection

#define VALUE_ARRAY_SET ObjectArraySet

#define VALUE_ITERATOR ObjectIterator

#define VALUE_LIST_ITERATOR ObjectListIterator

/* Abstract implementations (keys) */
#define ABSTRACT_COLLECTION AbstractByteCollection

#define ABSTRACT_SET AbstractByteSet

#define ABSTRACT_SORTED_SET AbstractByteSortedSet
#define ABSTRACT_FUNCTION AbstractByte2ObjectFunction
#define ABSTRACT_MAP AbstractByte2ObjectMap
#define ABSTRACT_FUNCTION AbstractByte2ObjectFunction
#define ABSTRACT_SORTED_MAP AbstractByte2ObjectSortedMap
#define ABSTRACT_LIST AbstractByteList

#define SUBLIST ByteSubList

#define ABSTRACT_PRIORITY_QUEUE AbstractBytePriorityQueue

#define ABSTRACT_STACK AbstractByteStack

#define KEY_ABSTRACT_ITERATOR AbstractByteIterator

#define KEY_ABSTRACT_BIDI_ITERATOR AbstractByteBidirectionalIterator

#define KEY_ABSTRACT_LIST_ITERATOR AbstractByteListIterator

#if #keyclass(Object)
#define KEY_ABSTRACT_COMPARATOR Comparator

#else
#define KEY_ABSTRACT_COMPARATOR AbstractByteComparator

#endif
/* Abstract implementations (values) */
#define VALUE_ABSTRACT_COLLECTION AbstractObjectCollection

#define VALUE_ABSTRACT_ITERATOR AbstractObjectIterator

#define VALUE_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator

/* Static containers (keys) */
#define COLLECTIONS ByteCollections

#define SETS ByteSets

#define SORTED_SETS ByteSortedSets

#define LISTS ByteLists

#define MAPS Byte2ObjectMaps
#define FUNCTIONS Byte2ObjectFunctions
#define SORTED_MAPS Byte2ObjectSortedMaps
#define PRIORITY_QUEUES BytePriorityQueues

#define HEAPS ByteHeaps

#define SEMI_INDIRECT_HEAPS ByteSemiIndirectHeaps

#define INDIRECT_HEAPS ByteIndirectHeaps

#define ARRAYS ByteArrays

#define ITERATORS ByteIterators

#define COMPARATORS ByteComparators

/* Static containers (values) */
#define VALUE_COLLECTIONS ObjectCollections

#define VALUE_SETS ObjectSets

#define VALUE_ARRAYS ObjectArrays

/* Implementations */
#define OPEN_HASH_SET ByteOpenHashSet

#define OPEN_HASH_MAP Byte2ObjectOpenHashMap

#define ARRAY_SET ByteArraySet

#define ARRAY_MAP Byte2ObjectArrayMap

#define LINKED_OPEN_HASH_SET ByteLinkedOpenHashSet

#define AVL_TREE_SET ByteAVLTreeSet

#define RB_TREE_SET ByteRBTreeSet

#define AVL_TREE_MAP Byte2ObjectAVLTreeMap

#define RB_TREE_MAP Byte2ObjectRBTreeMap

#define ARRAY_LIST ByteArrayList

#define ARRAY_FRONT_CODED_LIST ByteArrayFrontCodedList

#define HEAP_PRIORITY_QUEUE ByteHeapPriorityQueue

#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE ByteHeapSemiIndirectPriorityQueue

#define HEAP_INDIRECT_PRIORITY_QUEUE ByteHeapIndirectPriorityQueue

#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE ByteHeapSesquiIndirectDoublePriorityQueue

#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE ByteHeapIndirectDoublePriorityQueue

#define ARRAY_PRIORITY_QUEUE ByteArrayPriorityQueue

#define ARRAY_INDIRECT_PRIORITY_QUEUE ByteArrayIndirectPriorityQueue

#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE ByteArrayIndirectDoublePriorityQueue

/* Synchronized wrappers */
#define SYNCHRONIZED_COLLECTION SynchronizedByteCollection

#define SYNCHRONIZED_SET SynchronizedByteSet

#define SYNCHRONIZED_SORTED_SET SynchronizedByteSortedSet

#define SYNCHRONIZED_FUNCTION SynchronizedByte2ObjectFunction

#define SYNCHRONIZED_MAP SynchronizedByte2ObjectMap

#define SYNCHRONIZED_LIST SynchronizedByteList

/* Unmodifiable wrappers */
#define UNMODIFIABLE_COLLECTION UnmodifiableByteCollection

#define UNMODIFIABLE_SET UnmodifiableByteSet

#define UNMODIFIABLE_SORTED_SET UnmodifiableByteSortedSet

#define UNMODIFIABLE_FUNCTION UnmodifiableByte2ObjectFunction

#define UNMODIFIABLE_MAP UnmodifiableByte2ObjectMap

#define UNMODIFIABLE_LIST UnmodifiableByteList

#define UNMODIFIABLE_KEY_ITERATOR UnmodifiableByteIterator

#define UNMODIFIABLE_KEY_BIDI_ITERATOR UnmodifiableByteBidirectionalIterator

#define UNMODIFIABLE_KEY_LIST_ITERATOR UnmodifiableByteListIterator

/* Other wrappers */
#define KEY_READER_WRAPPER ByteReaderWrapper

#define KEY_DATA_INPUT_WRAPPER ByteDataInputWrapper

/* Methods (keys) */
#define NEXT_KEY nextByte
#define PREV_KEY previousByte
#define FIRST_KEY firstByteKey
#define LAST_KEY lastByteKey
#define GET_KEY getByte
#define REMOVE_KEY removeByte
#define READ_KEY readByte
#define WRITE_KEY writeByte
#define DEQUEUE dequeueByte
#define SUBLIST_METHOD byteSubList
#define SINGLETON_METHOD byteSingleton

#define FIRST firstByte
#define LAST lastByte
#define TOP topByte
#define PEEK peekByte
#define POP popByte
#define KEY_ITERATOR_METHOD byteIterator

#define KEY_LIST_ITERATOR_METHOD byteListIterator

#define KEY_EMPTY_ITERATOR_METHOD emptyByteIterator

#define AS_KEY_ITERATOR asByteIterator

#define TO_KEY_ARRAY toByteArray
#define ENTRY_GET_KEY getByteKey
#define PARSE_KEY parseByte
#define LOAD_KEYS loadBytes
#define STORE_KEYS storeBytes
/* Methods (values) */
#define NEXT_VALUE next
#define PREV_VALUE previous
#define READ_VALUE readObject
#define WRITE_VALUE writeObject
#define VALUE_ITERATOR_METHOD objectIterator

#define ENTRY_GET_VALUE getValue
/* Methods (keys/values) */
#define ENTRYSET byte2ObjectEntrySet
/* Methods that have special names depending on keys (but the special names depend on values) */
#if #keyclass(Object) || #keyclass(Reference)
#define GET_VALUE get
#define REMOVE_VALUE remove
#else
#define GET_VALUE get
#define REMOVE_VALUE remove
#endif
/* Equality */
#if #keyclass(Object)
#ifdef Custom
#define KEY_EQUALS(x,y) ( strategy.equals( (x), (y) ) )
#define KEY_EQUALS_HASH(x,h,y) ( (y) != HashCommon.REMOVED && (h) == strategy.hashCode(y) && strategy.equals((x), (y)) )
#else
#define KEY_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )
#define KEY_EQUALS_NOT_NULL(x,y) ( (x).equals(y) )
#define KEY_EQUALS_HASH(x,h,y) ( (y) == null ? (x) == null : (h) == (y).hashCode() && (y).equals(x) )
#endif
#else
#define KEY_EQUALS(x,y) ( (x) == (y) )
#define KEY_EQUALS_NOT_NULL(x,y) ( (x) == (y) )
#define KEY_EQUALS_HASH(x,h,y) ( (x) == (y) )
#endif

#if #valueclass(Object)
#define VALUE_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )
#else
#define VALUE_EQUALS(x,y) ( (x) == (y) )
#endif

/* Object/Reference-only definitions (keys) */
#if #keyclass(Object) || #keyclass(Reference)
#define REMOVE remove
#define KEY_OBJ2TYPE(x) (x)
#define KEY_CLASS2TYPE(x) (x)
#define KEY2OBJ(x) (x)
#if #keyclass(Object)
#ifdef Custom
#define KEY2INT(x) ( strategy.hashCode(x) )
#else
#define KEY2INT(x) ( (x) == null ? 0 : (x).hashCode() )
#endif
#else
#define KEY2INT(x) (System.identityHashCode(x))
#endif
#define KEY_CMP(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) )
#define KEY_LESS(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) < 0 )
#define KEY_LESSEQ(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) <= 0 )
#define KEY_NULL (null)
#else
/* Primitive-type-only definitions (keys) */
#define REMOVE rem
#define KEY_CLASS2TYPE(x) ((x).KEY_VALUE())
#define KEY_OBJ2TYPE(x) (KEY_CLASS2TYPE((KEY_CLASS)(x)))
#define KEY2OBJ(x) (KEY_CLASS.valueOf(x))
#if #keyclass(Boolean)
#define KEY_CMP(x,y) ( !(x) && (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )
#define KEY_LESS(x,y) ( !(x) && (y) )
#define KEY_LESSEQ(x,y) ( !(x) || (y) )
#else
#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )
#define KEY_LESS(x,y) ( (x) < (y) )
#define KEY_LESSEQ(x,y) ( (x) <= (y) )
#endif
#if #keyclass(Float) || #keyclass(Double) || #keyclass(Long)
#define KEY_NULL (0)
#define KEY2INT(x) it.unimi.dsi.fastutil.HashCommon.byte2int(x)
#elif #keyclass(Boolean)
#define KEY_NULL (false)
#define KEY2INT(x) (x ? 1231 : 1237)
#else
#if #keyclass(Integer)
#define KEY_NULL (0)
#else
#define KEY_NULL ((KEY_TYPE)0)
#endif
#define KEY2INT(x) (x)
#endif
#endif
/* Object/Reference-only definitions (values) */
#if #valueclass(Object) || #valueclass(Reference)
#define VALUE_OBJ2TYPE(x) (x)
#define VALUE_CLASS2TYPE(x) (x)
#define VALUE2OBJ(x) (x)
#if #valueclass(Object)
#define VALUE2INT(x) ( (x) == null ? 0 : (x).hashCode() )
#else
#define VALUE2INT(x) (System.identityHashCode(x))
#endif
#define VALUE_NULL (null)
#define OBJECT_DEFAULT_RETURN_VALUE (this.defRetValue)
#else
/* Primitive-type-only definitions (values) */
#define VALUE_CLASS2TYPE(x) ((x).VALUE_VALUE())
#define VALUE_OBJ2TYPE(x) (VALUE_CLASS2TYPE((VALUE_CLASS)(x)))
#define VALUE2OBJ(x) (VALUE_CLASS.valueOf(x))
#if #valueclass(Float) || #valueclass(Double) || #valueclass(Long)
#define VALUE_NULL (0)
#define VALUE2INT(x) it.unimi.dsi.fastutil.HashCommon.Object2int(x)
#elif #valueclass(Boolean)
#define VALUE_NULL (false)
#define VALUE2INT(x) (x ? 1231 : 1237)
#else
#if #valueclass(Integer)
#define VALUE_NULL (0)
#else
#define VALUE_NULL ((VALUE_TYPE)0)
#endif
#define VALUE2INT(x) (x)
#endif
#define OBJECT_DEFAULT_RETURN_VALUE (null)
#endif
#include "BinIOFragment.drv"


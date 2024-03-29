/* Generic definitions */


#define PACKAGE it.unimi.dsi.fastutil.shorts
#define VALUE_PACKAGE it.unimi.dsi.fastutil.ints
/* Assertions (useful to generate conditional code) */
#unassert keyclass
#assert keyclass(Short)
#unassert keys
 #assert keys(primitive)
#unassert valueclass
#assert valueclass(Integer)
#unassert values
 #assert values(primitive)
/* Current type and class (and size, if applicable) */
#define KEY_TYPE short
#define VALUE_TYPE int
#define KEY_CLASS Short
#define VALUE_CLASS Integer
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
#define KEY_VALUE shortValue
#define VALUE_VALUE intValue
/* Interfaces (keys) */
#define COLLECTION ShortCollection

#define SET ShortSet

#define SORTED_SET ShortSortedSet

#define STD_SORTED_SET ShortSortedSet

#define FUNCTION Short2IntFunction
#define MAP Short2IntMap
#define SORTED_MAP Short2IntSortedMap
#if #keyclass(Object) || #keyclass(Reference)
#define STD_SORTED_MAP SortedMap

#else
#define STD_SORTED_MAP Short2IntSortedMap

#endif
#define LIST ShortList

#define STACK ShortStack

#define PRIORITY_QUEUE ShortPriorityQueue

#define INDIRECT_PRIORITY_QUEUE ShortIndirectPriorityQueue

#define INDIRECT_DOUBLE_PRIORITY_QUEUE ShortIndirectDoublePriorityQueue

#define KEY_ITERATOR ShortIterator

#define KEY_ITERABLE ShortIterable

#define KEY_BIDI_ITERATOR ShortBidirectionalIterator

#define KEY_LIST_ITERATOR ShortListIterator

#define STD_KEY_ITERATOR ShortIterator

#define KEY_COMPARATOR ShortComparator

/* Interfaces (values) */
#define VALUE_COLLECTION IntCollection

#define VALUE_ARRAY_SET IntArraySet

#define VALUE_ITERATOR IntIterator

#define VALUE_LIST_ITERATOR IntListIterator

/* Abstract implementations (keys) */
#define ABSTRACT_COLLECTION AbstractShortCollection

#define ABSTRACT_SET AbstractShortSet

#define ABSTRACT_SORTED_SET AbstractShortSortedSet
#define ABSTRACT_FUNCTION AbstractShort2IntFunction
#define ABSTRACT_MAP AbstractShort2IntMap
#define ABSTRACT_FUNCTION AbstractShort2IntFunction
#define ABSTRACT_SORTED_MAP AbstractShort2IntSortedMap
#define ABSTRACT_LIST AbstractShortList

#define SUBLIST ShortSubList

#define ABSTRACT_PRIORITY_QUEUE AbstractShortPriorityQueue

#define ABSTRACT_STACK AbstractShortStack

#define KEY_ABSTRACT_ITERATOR AbstractShortIterator

#define KEY_ABSTRACT_BIDI_ITERATOR AbstractShortBidirectionalIterator

#define KEY_ABSTRACT_LIST_ITERATOR AbstractShortListIterator

#if #keyclass(Object)
#define KEY_ABSTRACT_COMPARATOR Comparator

#else
#define KEY_ABSTRACT_COMPARATOR AbstractShortComparator

#endif
/* Abstract implementations (values) */
#define VALUE_ABSTRACT_COLLECTION AbstractIntCollection

#define VALUE_ABSTRACT_ITERATOR AbstractIntIterator

#define VALUE_ABSTRACT_BIDI_ITERATOR AbstractIntBidirectionalIterator

/* Static containers (keys) */
#define COLLECTIONS ShortCollections

#define SETS ShortSets

#define SORTED_SETS ShortSortedSets

#define LISTS ShortLists

#define MAPS Short2IntMaps
#define FUNCTIONS Short2IntFunctions
#define SORTED_MAPS Short2IntSortedMaps
#define PRIORITY_QUEUES ShortPriorityQueues

#define HEAPS ShortHeaps

#define SEMI_INDIRECT_HEAPS ShortSemiIndirectHeaps

#define INDIRECT_HEAPS ShortIndirectHeaps

#define ARRAYS ShortArrays

#define ITERATORS ShortIterators

#define COMPARATORS ShortComparators

/* Static containers (values) */
#define VALUE_COLLECTIONS IntCollections

#define VALUE_SETS IntSets

#define VALUE_ARRAYS IntArrays

/* Implementations */
#define OPEN_HASH_SET ShortOpenHashSet

#define OPEN_HASH_MAP Short2IntOpenHashMap

#define ARRAY_SET ShortArraySet

#define ARRAY_MAP Short2IntArrayMap

#define LINKED_OPEN_HASH_SET ShortLinkedOpenHashSet

#define AVL_TREE_SET ShortAVLTreeSet

#define RB_TREE_SET ShortRBTreeSet

#define AVL_TREE_MAP Short2IntAVLTreeMap

#define RB_TREE_MAP Short2IntRBTreeMap

#define ARRAY_LIST ShortArrayList

#define ARRAY_FRONT_CODED_LIST ShortArrayFrontCodedList

#define HEAP_PRIORITY_QUEUE ShortHeapPriorityQueue

#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE ShortHeapSemiIndirectPriorityQueue

#define HEAP_INDIRECT_PRIORITY_QUEUE ShortHeapIndirectPriorityQueue

#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE ShortHeapSesquiIndirectDoublePriorityQueue

#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE ShortHeapIndirectDoublePriorityQueue

#define ARRAY_PRIORITY_QUEUE ShortArrayPriorityQueue

#define ARRAY_INDIRECT_PRIORITY_QUEUE ShortArrayIndirectPriorityQueue

#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE ShortArrayIndirectDoublePriorityQueue

/* Synchronized wrappers */
#define SYNCHRONIZED_COLLECTION SynchronizedShortCollection

#define SYNCHRONIZED_SET SynchronizedShortSet

#define SYNCHRONIZED_SORTED_SET SynchronizedShortSortedSet

#define SYNCHRONIZED_FUNCTION SynchronizedShort2IntFunction

#define SYNCHRONIZED_MAP SynchronizedShort2IntMap

#define SYNCHRONIZED_LIST SynchronizedShortList

/* Unmodifiable wrappers */
#define UNMODIFIABLE_COLLECTION UnmodifiableShortCollection

#define UNMODIFIABLE_SET UnmodifiableShortSet

#define UNMODIFIABLE_SORTED_SET UnmodifiableShortSortedSet

#define UNMODIFIABLE_FUNCTION UnmodifiableShort2IntFunction

#define UNMODIFIABLE_MAP UnmodifiableShort2IntMap

#define UNMODIFIABLE_LIST UnmodifiableShortList

#define UNMODIFIABLE_KEY_ITERATOR UnmodifiableShortIterator

#define UNMODIFIABLE_KEY_BIDI_ITERATOR UnmodifiableShortBidirectionalIterator

#define UNMODIFIABLE_KEY_LIST_ITERATOR UnmodifiableShortListIterator

/* Other wrappers */
#define KEY_READER_WRAPPER ShortReaderWrapper

#define KEY_DATA_INPUT_WRAPPER ShortDataInputWrapper

/* Methods (keys) */
#define NEXT_KEY nextShort
#define PREV_KEY previousShort
#define FIRST_KEY firstShortKey
#define LAST_KEY lastShortKey
#define GET_KEY getShort
#define REMOVE_KEY removeShort
#define READ_KEY readShort
#define WRITE_KEY writeShort
#define DEQUEUE dequeueShort
#define SUBLIST_METHOD shortSubList
#define SINGLETON_METHOD shortSingleton

#define FIRST firstShort
#define LAST lastShort
#define TOP topShort
#define PEEK peekShort
#define POP popShort
#define KEY_ITERATOR_METHOD shortIterator

#define KEY_LIST_ITERATOR_METHOD shortListIterator

#define KEY_EMPTY_ITERATOR_METHOD emptyShortIterator

#define AS_KEY_ITERATOR asShortIterator

#define TO_KEY_ARRAY toShortArray
#define ENTRY_GET_KEY getShortKey
#define PARSE_KEY parseShort
#define LOAD_KEYS loadShorts
#define STORE_KEYS storeShorts
/* Methods (values) */
#define NEXT_VALUE nextInt
#define PREV_VALUE previousInt
#define READ_VALUE readInt
#define WRITE_VALUE writeInt
#define VALUE_ITERATOR_METHOD intIterator

#define ENTRY_GET_VALUE getIntValue
/* Methods (keys/values) */
#define ENTRYSET short2IntEntrySet
/* Methods that have special names depending on keys (but the special names depend on values) */
#if #keyclass(Object) || #keyclass(Reference)
#define GET_VALUE getInt
#define REMOVE_VALUE removeInt
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
#define KEY2INT(x) it.unimi.dsi.fastutil.HashCommon.short2int(x)
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
#define VALUE2INT(x) it.unimi.dsi.fastutil.HashCommon.int2int(x)
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
#include "RBTreeMap.drv"


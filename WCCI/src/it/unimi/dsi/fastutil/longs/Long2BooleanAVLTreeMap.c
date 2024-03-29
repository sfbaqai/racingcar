/* Generic definitions */


#define PACKAGE it.unimi.dsi.fastutil.longs
#define VALUE_PACKAGE it.unimi.dsi.fastutil.booleans
/* Assertions (useful to generate conditional code) */
#unassert keyclass
#assert keyclass(Long)
#unassert keys
 #assert keys(primitive)
#unassert valueclass
#assert valueclass(Boolean)
#unassert values
 #assert values(primitive)
/* Current type and class (and size, if applicable) */
#define KEY_TYPE long
#define VALUE_TYPE boolean
#define KEY_CLASS Long
#define VALUE_CLASS Boolean
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
#define KEY_VALUE longValue
#define VALUE_VALUE booleanValue
/* Interfaces (keys) */
#define COLLECTION LongCollection

#define SET LongSet

#define SORTED_SET LongSortedSet

#define STD_SORTED_SET LongSortedSet

#define FUNCTION Long2BooleanFunction
#define MAP Long2BooleanMap
#define SORTED_MAP Long2BooleanSortedMap
#if #keyclass(Object) || #keyclass(Reference)
#define STD_SORTED_MAP SortedMap

#else
#define STD_SORTED_MAP Long2BooleanSortedMap

#endif
#define LIST LongList

#define STACK LongStack

#define PRIORITY_QUEUE LongPriorityQueue

#define INDIRECT_PRIORITY_QUEUE LongIndirectPriorityQueue

#define INDIRECT_DOUBLE_PRIORITY_QUEUE LongIndirectDoublePriorityQueue

#define KEY_ITERATOR LongIterator

#define KEY_ITERABLE LongIterable

#define KEY_BIDI_ITERATOR LongBidirectionalIterator

#define KEY_LIST_ITERATOR LongListIterator

#define STD_KEY_ITERATOR LongIterator

#define KEY_COMPARATOR LongComparator

/* Interfaces (values) */
#define VALUE_COLLECTION BooleanCollection

#define VALUE_ARRAY_SET BooleanArraySet

#define VALUE_ITERATOR BooleanIterator

#define VALUE_LIST_ITERATOR BooleanListIterator

/* Abstract implementations (keys) */
#define ABSTRACT_COLLECTION AbstractLongCollection

#define ABSTRACT_SET AbstractLongSet

#define ABSTRACT_SORTED_SET AbstractLongSortedSet
#define ABSTRACT_FUNCTION AbstractLong2BooleanFunction
#define ABSTRACT_MAP AbstractLong2BooleanMap
#define ABSTRACT_FUNCTION AbstractLong2BooleanFunction
#define ABSTRACT_SORTED_MAP AbstractLong2BooleanSortedMap
#define ABSTRACT_LIST AbstractLongList

#define SUBLIST LongSubList

#define ABSTRACT_PRIORITY_QUEUE AbstractLongPriorityQueue

#define ABSTRACT_STACK AbstractLongStack

#define KEY_ABSTRACT_ITERATOR AbstractLongIterator

#define KEY_ABSTRACT_BIDI_ITERATOR AbstractLongBidirectionalIterator

#define KEY_ABSTRACT_LIST_ITERATOR AbstractLongListIterator

#if #keyclass(Object)
#define KEY_ABSTRACT_COMPARATOR Comparator

#else
#define KEY_ABSTRACT_COMPARATOR AbstractLongComparator

#endif
/* Abstract implementations (values) */
#define VALUE_ABSTRACT_COLLECTION AbstractBooleanCollection

#define VALUE_ABSTRACT_ITERATOR AbstractBooleanIterator

#define VALUE_ABSTRACT_BIDI_ITERATOR AbstractBooleanBidirectionalIterator

/* Static containers (keys) */
#define COLLECTIONS LongCollections

#define SETS LongSets

#define SORTED_SETS LongSortedSets

#define LISTS LongLists

#define MAPS Long2BooleanMaps
#define FUNCTIONS Long2BooleanFunctions
#define SORTED_MAPS Long2BooleanSortedMaps
#define PRIORITY_QUEUES LongPriorityQueues

#define HEAPS LongHeaps

#define SEMI_INDIRECT_HEAPS LongSemiIndirectHeaps

#define INDIRECT_HEAPS LongIndirectHeaps

#define ARRAYS LongArrays

#define ITERATORS LongIterators

#define COMPARATORS LongComparators

/* Static containers (values) */
#define VALUE_COLLECTIONS BooleanCollections

#define VALUE_SETS BooleanSets

#define VALUE_ARRAYS BooleanArrays

/* Implementations */
#define OPEN_HASH_SET LongOpenHashSet

#define OPEN_HASH_MAP Long2BooleanOpenHashMap

#define ARRAY_SET LongArraySet

#define ARRAY_MAP Long2BooleanArrayMap

#define LINKED_OPEN_HASH_SET LongLinkedOpenHashSet

#define AVL_TREE_SET LongAVLTreeSet

#define RB_TREE_SET LongRBTreeSet

#define AVL_TREE_MAP Long2BooleanAVLTreeMap

#define RB_TREE_MAP Long2BooleanRBTreeMap

#define ARRAY_LIST LongArrayList

#define ARRAY_FRONT_CODED_LIST LongArrayFrontCodedList

#define HEAP_PRIORITY_QUEUE LongHeapPriorityQueue

#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE LongHeapSemiIndirectPriorityQueue

#define HEAP_INDIRECT_PRIORITY_QUEUE LongHeapIndirectPriorityQueue

#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE LongHeapSesquiIndirectDoublePriorityQueue

#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE LongHeapIndirectDoublePriorityQueue

#define ARRAY_PRIORITY_QUEUE LongArrayPriorityQueue

#define ARRAY_INDIRECT_PRIORITY_QUEUE LongArrayIndirectPriorityQueue

#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE LongArrayIndirectDoublePriorityQueue

/* Synchronized wrappers */
#define SYNCHRONIZED_COLLECTION SynchronizedLongCollection

#define SYNCHRONIZED_SET SynchronizedLongSet

#define SYNCHRONIZED_SORTED_SET SynchronizedLongSortedSet

#define SYNCHRONIZED_FUNCTION SynchronizedLong2BooleanFunction

#define SYNCHRONIZED_MAP SynchronizedLong2BooleanMap

#define SYNCHRONIZED_LIST SynchronizedLongList

/* Unmodifiable wrappers */
#define UNMODIFIABLE_COLLECTION UnmodifiableLongCollection

#define UNMODIFIABLE_SET UnmodifiableLongSet

#define UNMODIFIABLE_SORTED_SET UnmodifiableLongSortedSet

#define UNMODIFIABLE_FUNCTION UnmodifiableLong2BooleanFunction

#define UNMODIFIABLE_MAP UnmodifiableLong2BooleanMap

#define UNMODIFIABLE_LIST UnmodifiableLongList

#define UNMODIFIABLE_KEY_ITERATOR UnmodifiableLongIterator

#define UNMODIFIABLE_KEY_BIDI_ITERATOR UnmodifiableLongBidirectionalIterator

#define UNMODIFIABLE_KEY_LIST_ITERATOR UnmodifiableLongListIterator

/* Other wrappers */
#define KEY_READER_WRAPPER LongReaderWrapper

#define KEY_DATA_INPUT_WRAPPER LongDataInputWrapper

/* Methods (keys) */
#define NEXT_KEY nextLong
#define PREV_KEY previousLong
#define FIRST_KEY firstLongKey
#define LAST_KEY lastLongKey
#define GET_KEY getLong
#define REMOVE_KEY removeLong
#define READ_KEY readLong
#define WRITE_KEY writeLong
#define DEQUEUE dequeueLong
#define SUBLIST_METHOD longSubList
#define SINGLETON_METHOD longSingleton

#define FIRST firstLong
#define LAST lastLong
#define TOP topLong
#define PEEK peekLong
#define POP popLong
#define KEY_ITERATOR_METHOD longIterator

#define KEY_LIST_ITERATOR_METHOD longListIterator

#define KEY_EMPTY_ITERATOR_METHOD emptyLongIterator

#define AS_KEY_ITERATOR asLongIterator

#define TO_KEY_ARRAY toLongArray
#define ENTRY_GET_KEY getLongKey
#define PARSE_KEY parseLong
#define LOAD_KEYS loadLongs
#define STORE_KEYS storeLongs
/* Methods (values) */
#define NEXT_VALUE nextBoolean
#define PREV_VALUE previousBoolean
#define READ_VALUE readBoolean
#define WRITE_VALUE writeBoolean
#define VALUE_ITERATOR_METHOD booleanIterator

#define ENTRY_GET_VALUE getBooleanValue
/* Methods (keys/values) */
#define ENTRYSET long2BooleanEntrySet
/* Methods that have special names depending on keys (but the special names depend on values) */
#if #keyclass(Object) || #keyclass(Reference)
#define GET_VALUE getBoolean
#define REMOVE_VALUE removeBoolean
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
#define KEY2INT(x) it.unimi.dsi.fastutil.HashCommon.long2int(x)
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
#define VALUE2INT(x) it.unimi.dsi.fastutil.HashCommon.boolean2int(x)
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
#include "AVLTreeMap.drv"


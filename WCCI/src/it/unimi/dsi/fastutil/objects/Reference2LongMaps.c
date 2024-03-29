/* Generic definitions */


#define PACKAGE it.unimi.dsi.fastutil.objects
#define VALUE_PACKAGE it.unimi.dsi.fastutil.longs
/* Assertions (useful to generate conditional code) */
#unassert keyclass
#assert keyclass(Reference)
#unassert keys
 #assert keys(reference)
#unassert valueclass
#assert valueclass(Long)
#unassert values
 #assert values(primitive)
/* Current type and class (and size, if applicable) */
#define KEY_TYPE Object
#define VALUE_TYPE long
#define KEY_CLASS Reference
#define VALUE_CLASS Long
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
#define KEY_VALUE ObjectValue
#define VALUE_VALUE longValue
/* Interfaces (keys) */
#define COLLECTION ReferenceCollection

#define SET ReferenceSet

#define SORTED_SET ReferenceSortedSet

#define STD_SORTED_SET SortedSet

#define FUNCTION Reference2LongFunction
#define MAP Reference2LongMap
#define SORTED_MAP Reference2LongSortedMap
#if #keyclass(Object) || #keyclass(Reference)
#define STD_SORTED_MAP SortedMap

#else
#define STD_SORTED_MAP Reference2LongSortedMap

#endif
#define LIST ReferenceList

#define STACK Stack

#define PRIORITY_QUEUE PriorityQueue

#define INDIRECT_PRIORITY_QUEUE IndirectPriorityQueue

#define INDIRECT_DOUBLE_PRIORITY_QUEUE IndirectDoublePriorityQueue

#define KEY_ITERATOR ObjectIterator

#define KEY_ITERABLE ObjectIterable

#define KEY_BIDI_ITERATOR ObjectBidirectionalIterator

#define KEY_LIST_ITERATOR ObjectListIterator

#define STD_KEY_ITERATOR Iterator

#define KEY_COMPARATOR Comparator

/* Interfaces (values) */
#define VALUE_COLLECTION LongCollection

#define VALUE_ARRAY_SET LongArraySet

#define VALUE_ITERATOR LongIterator

#define VALUE_LIST_ITERATOR LongListIterator

/* Abstract implementations (keys) */
#define ABSTRACT_COLLECTION AbstractReferenceCollection

#define ABSTRACT_SET AbstractReferenceSet

#define ABSTRACT_SORTED_SET AbstractReferenceSortedSet
#define ABSTRACT_FUNCTION AbstractReference2LongFunction
#define ABSTRACT_MAP AbstractReference2LongMap
#define ABSTRACT_FUNCTION AbstractReference2LongFunction
#define ABSTRACT_SORTED_MAP AbstractReference2LongSortedMap
#define ABSTRACT_LIST AbstractReferenceList

#define SUBLIST ReferenceSubList

#define ABSTRACT_PRIORITY_QUEUE AbstractPriorityQueue

#define ABSTRACT_STACK AbstractStack

#define KEY_ABSTRACT_ITERATOR AbstractObjectIterator

#define KEY_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator

#define KEY_ABSTRACT_LIST_ITERATOR AbstractObjectListIterator

#if #keyclass(Object)
#define KEY_ABSTRACT_COMPARATOR Comparator

#else
#define KEY_ABSTRACT_COMPARATOR AbstractReferenceComparator

#endif
/* Abstract implementations (values) */
#define VALUE_ABSTRACT_COLLECTION AbstractLongCollection

#define VALUE_ABSTRACT_ITERATOR AbstractLongIterator

#define VALUE_ABSTRACT_BIDI_ITERATOR AbstractLongBidirectionalIterator

/* Static containers (keys) */
#define COLLECTIONS ReferenceCollections

#define SETS ReferenceSets

#define SORTED_SETS ReferenceSortedSets

#define LISTS ReferenceLists

#define MAPS Reference2LongMaps
#define FUNCTIONS Reference2LongFunctions
#define SORTED_MAPS Reference2LongSortedMaps
#define PRIORITY_QUEUES ObjectPriorityQueues

#define HEAPS ObjectHeaps

#define SEMI_INDIRECT_HEAPS ObjectSemiIndirectHeaps

#define INDIRECT_HEAPS ObjectIndirectHeaps

#define ARRAYS ObjectArrays

#define ITERATORS ObjectIterators

#define COMPARATORS ObjectComparators

/* Static containers (values) */
#define VALUE_COLLECTIONS LongCollections

#define VALUE_SETS LongSets

#define VALUE_ARRAYS LongArrays

/* Implementations */
#define OPEN_HASH_SET ReferenceOpenHashSet

#define OPEN_HASH_MAP Reference2LongOpenHashMap

#define ARRAY_SET ReferenceArraySet

#define ARRAY_MAP Reference2LongArrayMap

#define LINKED_OPEN_HASH_SET ReferenceLinkedOpenHashSet

#define AVL_TREE_SET ReferenceAVLTreeSet

#define RB_TREE_SET ReferenceRBTreeSet

#define AVL_TREE_MAP Reference2LongAVLTreeMap

#define RB_TREE_MAP Reference2LongRBTreeMap

#define ARRAY_LIST ReferenceArrayList

#define ARRAY_FRONT_CODED_LIST ReferenceArrayFrontCodedList

#define HEAP_PRIORITY_QUEUE ObjectHeapPriorityQueue

#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE ObjectHeapSemiIndirectPriorityQueue

#define HEAP_INDIRECT_PRIORITY_QUEUE ObjectHeapIndirectPriorityQueue

#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE ObjectHeapSesquiIndirectDoublePriorityQueue

#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE ObjectHeapIndirectDoublePriorityQueue

#define ARRAY_PRIORITY_QUEUE ObjectArrayPriorityQueue

#define ARRAY_INDIRECT_PRIORITY_QUEUE ObjectArrayIndirectPriorityQueue

#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE ObjectArrayIndirectDoublePriorityQueue

/* Synchronized wrappers */
#define SYNCHRONIZED_COLLECTION SynchronizedReferenceCollection

#define SYNCHRONIZED_SET SynchronizedReferenceSet

#define SYNCHRONIZED_SORTED_SET SynchronizedReferenceSortedSet

#define SYNCHRONIZED_FUNCTION SynchronizedReference2LongFunction

#define SYNCHRONIZED_MAP SynchronizedReference2LongMap

#define SYNCHRONIZED_LIST SynchronizedReferenceList

/* Unmodifiable wrappers */
#define UNMODIFIABLE_COLLECTION UnmodifiableReferenceCollection

#define UNMODIFIABLE_SET UnmodifiableReferenceSet

#define UNMODIFIABLE_SORTED_SET UnmodifiableReferenceSortedSet

#define UNMODIFIABLE_FUNCTION UnmodifiableReference2LongFunction

#define UNMODIFIABLE_MAP UnmodifiableReference2LongMap

#define UNMODIFIABLE_LIST UnmodifiableReferenceList

#define UNMODIFIABLE_KEY_ITERATOR UnmodifiableReferenceIterator

#define UNMODIFIABLE_KEY_BIDI_ITERATOR UnmodifiableReferenceBidirectionalIterator

#define UNMODIFIABLE_KEY_LIST_ITERATOR UnmodifiableReferenceListIterator

/* Other wrappers */
#define KEY_READER_WRAPPER ReferenceReaderWrapper

#define KEY_DATA_INPUT_WRAPPER ReferenceDataInputWrapper

/* Methods (keys) */
#define NEXT_KEY next
#define PREV_KEY previous
#define FIRST_KEY firstKey
#define LAST_KEY lastKey
#define GET_KEY get
#define REMOVE_KEY remove
#define READ_KEY readObject
#define WRITE_KEY writeObject
#define DEQUEUE dequeue
#define SUBLIST_METHOD referenceSubList
#define SINGLETON_METHOD referenceSingleton

#define FIRST first
#define LAST last
#define TOP top
#define PEEK peek
#define POP pop
#define KEY_ITERATOR_METHOD objectIterator

#define KEY_LIST_ITERATOR_METHOD objectListIterator

#define KEY_EMPTY_ITERATOR_METHOD emptyObjectIterator

#define AS_KEY_ITERATOR asObjectIterator

#define TO_KEY_ARRAY toArray
#define ENTRY_GET_KEY getKey
#define PARSE_KEY parse
#define LOAD_KEYS loads
#define STORE_KEYS stores
/* Methods (values) */
#define NEXT_VALUE nextLong
#define PREV_VALUE previousLong
#define READ_VALUE readLong
#define WRITE_VALUE writeLong
#define VALUE_ITERATOR_METHOD longIterator

#define ENTRY_GET_VALUE getLongValue
/* Methods (keys/values) */
#define ENTRYSET reference2LongEntrySet
/* Methods that have special names depending on keys (but the special names depend on values) */
#if #keyclass(Object) || #keyclass(Reference)
#define GET_VALUE getLong
#define REMOVE_VALUE removeLong
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
#define KEY2INT(x) it.unimi.dsi.fastutil.HashCommon.Object2int(x)
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
#define VALUE2INT(x) it.unimi.dsi.fastutil.HashCommon.long2int(x)
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
#include "Maps.drv"


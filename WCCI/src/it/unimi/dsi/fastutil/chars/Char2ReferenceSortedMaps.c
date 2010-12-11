/* Generic definitions */


#define PACKAGE it.unimi.dsi.fastutil.chars
#define VALUE_PACKAGE it.unimi.dsi.fastutil.objects
/* Assertions (useful to generate conditional code) */
#unassert keyclass
#assert keyclass(Character)
#unassert keys
 #assert keys(primitive)
#unassert valueclass
#assert valueclass(Reference)
#unassert values
 #assert values(reference)
/* Current type and class (and size, if applicable) */
#define KEY_TYPE char
#define VALUE_TYPE Object
#define KEY_CLASS Character
#define VALUE_CLASS Reference
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
#define KEY_VALUE charValue
#define VALUE_VALUE ObjectValue
/* Interfaces (keys) */
#define COLLECTION CharCollection

#define SET CharSet

#define SORTED_SET CharSortedSet

#define STD_SORTED_SET CharSortedSet

#define FUNCTION Char2ReferenceFunction
#define MAP Char2ReferenceMap
#define SORTED_MAP Char2ReferenceSortedMap
#if #keyclass(Object) || #keyclass(Reference)
#define STD_SORTED_MAP SortedMap

#else
#define STD_SORTED_MAP Char2ReferenceSortedMap

#endif
#define LIST CharList

#define STACK CharStack

#define PRIORITY_QUEUE CharPriorityQueue

#define INDIRECT_PRIORITY_QUEUE CharIndirectPriorityQueue

#define INDIRECT_DOUBLE_PRIORITY_QUEUE CharIndirectDoublePriorityQueue

#define KEY_ITERATOR CharIterator

#define KEY_ITERABLE CharIterable

#define KEY_BIDI_ITERATOR CharBidirectionalIterator

#define KEY_LIST_ITERATOR CharListIterator

#define STD_KEY_ITERATOR CharIterator

#define KEY_COMPARATOR CharComparator

/* Interfaces (values) */
#define VALUE_COLLECTION ReferenceCollection

#define VALUE_ARRAY_SET ReferenceArraySet

#define VALUE_ITERATOR ObjectIterator

#define VALUE_LIST_ITERATOR ObjectListIterator

/* Abstract implementations (keys) */
#define ABSTRACT_COLLECTION AbstractCharCollection

#define ABSTRACT_SET AbstractCharSet

#define ABSTRACT_SORTED_SET AbstractCharSortedSet
#define ABSTRACT_FUNCTION AbstractChar2ReferenceFunction
#define ABSTRACT_MAP AbstractChar2ReferenceMap
#define ABSTRACT_FUNCTION AbstractChar2ReferenceFunction
#define ABSTRACT_SORTED_MAP AbstractChar2ReferenceSortedMap
#define ABSTRACT_LIST AbstractCharList

#define SUBLIST CharSubList

#define ABSTRACT_PRIORITY_QUEUE AbstractCharPriorityQueue

#define ABSTRACT_STACK AbstractCharStack

#define KEY_ABSTRACT_ITERATOR AbstractCharIterator

#define KEY_ABSTRACT_BIDI_ITERATOR AbstractCharBidirectionalIterator

#define KEY_ABSTRACT_LIST_ITERATOR AbstractCharListIterator

#if #keyclass(Object)
#define KEY_ABSTRACT_COMPARATOR Comparator

#else
#define KEY_ABSTRACT_COMPARATOR AbstractCharComparator

#endif
/* Abstract implementations (values) */
#define VALUE_ABSTRACT_COLLECTION AbstractReferenceCollection

#define VALUE_ABSTRACT_ITERATOR AbstractObjectIterator

#define VALUE_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator

/* Static containers (keys) */
#define COLLECTIONS CharCollections

#define SETS CharSets

#define SORTED_SETS CharSortedSets

#define LISTS CharLists

#define MAPS Char2ReferenceMaps
#define FUNCTIONS Char2ReferenceFunctions
#define SORTED_MAPS Char2ReferenceSortedMaps
#define PRIORITY_QUEUES CharPriorityQueues

#define HEAPS CharHeaps

#define SEMI_INDIRECT_HEAPS CharSemiIndirectHeaps

#define INDIRECT_HEAPS CharIndirectHeaps

#define ARRAYS CharArrays

#define ITERATORS CharIterators

#define COMPARATORS CharComparators

/* Static containers (values) */
#define VALUE_COLLECTIONS ReferenceCollections

#define VALUE_SETS ReferenceSets

#define VALUE_ARRAYS ObjectArrays

/* Implementations */
#define OPEN_HASH_SET CharOpenHashSet

#define OPEN_HASH_MAP Char2ReferenceOpenHashMap

#define ARRAY_SET CharArraySet

#define ARRAY_MAP Char2ReferenceArrayMap

#define LINKED_OPEN_HASH_SET CharLinkedOpenHashSet

#define AVL_TREE_SET CharAVLTreeSet

#define RB_TREE_SET CharRBTreeSet

#define AVL_TREE_MAP Char2ReferenceAVLTreeMap

#define RB_TREE_MAP Char2ReferenceRBTreeMap

#define ARRAY_LIST CharArrayList

#define ARRAY_FRONT_CODED_LIST CharArrayFrontCodedList

#define HEAP_PRIORITY_QUEUE CharHeapPriorityQueue

#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE CharHeapSemiIndirectPriorityQueue

#define HEAP_INDIRECT_PRIORITY_QUEUE CharHeapIndirectPriorityQueue

#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE CharHeapSesquiIndirectDoublePriorityQueue

#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE CharHeapIndirectDoublePriorityQueue

#define ARRAY_PRIORITY_QUEUE CharArrayPriorityQueue

#define ARRAY_INDIRECT_PRIORITY_QUEUE CharArrayIndirectPriorityQueue

#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE CharArrayIndirectDoublePriorityQueue

/* Synchronized wrappers */
#define SYNCHRONIZED_COLLECTION SynchronizedCharCollection

#define SYNCHRONIZED_SET SynchronizedCharSet

#define SYNCHRONIZED_SORTED_SET SynchronizedCharSortedSet

#define SYNCHRONIZED_FUNCTION SynchronizedChar2ReferenceFunction

#define SYNCHRONIZED_MAP SynchronizedChar2ReferenceMap

#define SYNCHRONIZED_LIST SynchronizedCharList

/* Unmodifiable wrappers */
#define UNMODIFIABLE_COLLECTION UnmodifiableCharCollection

#define UNMODIFIABLE_SET UnmodifiableCharSet

#define UNMODIFIABLE_SORTED_SET UnmodifiableCharSortedSet

#define UNMODIFIABLE_FUNCTION UnmodifiableChar2ReferenceFunction

#define UNMODIFIABLE_MAP UnmodifiableChar2ReferenceMap

#define UNMODIFIABLE_LIST UnmodifiableCharList

#define UNMODIFIABLE_KEY_ITERATOR UnmodifiableCharIterator

#define UNMODIFIABLE_KEY_BIDI_ITERATOR UnmodifiableCharBidirectionalIterator

#define UNMODIFIABLE_KEY_LIST_ITERATOR UnmodifiableCharListIterator

/* Other wrappers */
#define KEY_READER_WRAPPER CharReaderWrapper

#define KEY_DATA_INPUT_WRAPPER CharDataInputWrapper

/* Methods (keys) */
#define NEXT_KEY nextChar
#define PREV_KEY previousChar
#define FIRST_KEY firstCharKey
#define LAST_KEY lastCharKey
#define GET_KEY getChar
#define REMOVE_KEY removeChar
#define READ_KEY readChar
#define WRITE_KEY writeChar
#define DEQUEUE dequeueChar
#define SUBLIST_METHOD charSubList
#define SINGLETON_METHOD charSingleton

#define FIRST firstChar
#define LAST lastChar
#define TOP topChar
#define PEEK peekChar
#define POP popChar
#define KEY_ITERATOR_METHOD charIterator

#define KEY_LIST_ITERATOR_METHOD charListIterator

#define KEY_EMPTY_ITERATOR_METHOD emptyCharIterator

#define AS_KEY_ITERATOR asCharIterator

#define TO_KEY_ARRAY toCharArray
#define ENTRY_GET_KEY getCharKey
#define PARSE_KEY parseChar
#define LOAD_KEYS loadChars
#define STORE_KEYS storeChars
/* Methods (values) */
#define NEXT_VALUE next
#define PREV_VALUE previous
#define READ_VALUE readObject
#define WRITE_VALUE writeObject
#define VALUE_ITERATOR_METHOD objectIterator

#define ENTRY_GET_VALUE getValue
/* Methods (keys/values) */
#define ENTRYSET char2ReferenceEntrySet
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
#define KEY2INT(x) it.unimi.dsi.fastutil.HashCommon.char2int(x)
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
#include "SortedMaps.drv"


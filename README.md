[![GitHub stars](https://img.shields.io/github/stars/indy256/codelibrary.svg?style=flat&label=star)](https://github.com/indy256/codelibrary/)
[![Java CI](https://github.com/indy256/codelibrary/workflows/Java%20CI/badge.svg)](https://github.com/indy256/codelibrary/actions?query=workflow%3A%22Java+CI%22)
[![C++ CI](https://github.com/indy256/codelibrary/workflows/C++%20CI/badge.svg)](https://github.com/indy256/codelibrary/actions?query=workflow%3A%22C%2B%2B+CI%22)
[![License](https://img.shields.io/badge/license-UNLICENSE-green.svg)](https://github.com/indy256/codelibrary/blob/master/UNLICENSE)

## [codelibrary](https://github.com/indy256/codelibrary) in Kotlin(automatically converted by IDEA)
**Many of them haven't been verified. Please refer to the original java versions if you have any trouble.**
Many thanks to the original author of java version, [indy256](https://github.com/indy256).

### Collection of algorithms and data structures in C++ and Java

#### Data structures
+ [x] Segment tree [c++](cpp/structures/segment_tree.h) [java](java/structures/SegmentTree.java) [kotlin](kotlin/SegmentTree.kt) [**kotlin**](kotlin/structures/SegmentTree.kt)
+ [x] Segment tree without recursion [c++](cpp/structures/segment_tree_without_recursion.cpp) [java](java/structures/SegmentTreeWithoutRecursion.java) [**kotlin**](kotlin/structures/SegmentTreeWithoutRecursion.kt)
+ [x] 2d tree [c++](cpp/structures/tree_2d.cpp) [java](java/structures/Tree2d.java) [**kotlin**](kotlin/structures/Tree2d.kt)
+ [x] Fenwick tree [c++](cpp/structures/fenwick_tree.cpp) [java](java/structures/FenwickTree.java) [kotlin](kotlin/FenwickTree.kt) [**kotlin**](kotlin/structures/FenwickTree.kt)
+ [x] Fenwick tree with extended operations [c++](cpp/structures/fenwick_tree_interval.cpp) [java](java/structures/FenwickTreeExtended.java) [**kotlin**](kotlin/structures/FenwickTreeExtended.kt)
+ [x] Persistent tree [java](java/structures/PersistentTree.java) [kotlin](kotlin/PersistentTree.kt) [**kotlin**](kotlin/structures/PersistentTree.kt)
+ [x] Centroid decomposition [c++](cpp/structures/centroid_decomposition.cpp) [java](java/structures/CentroidDecomposition.java) [**kotlin**](kotlin/structures/CentroidDecomposition.kt)
+ [x] Heavy/light decomposition [c++](cpp/structures/heavy_light_decomposition.cpp) [java](java/structures/HeavyLight.java) [**kotlin**](kotlin/structures/HeavyLight.kt)
+ [x] Link/cut tree [c++](cpp/structures/link_cut_tree.cpp) [java](java/structures/LinkCutTree.java) [**kotlin**](kotlin/structures/LinkCutTree.kt)
+ [x] Link/cut tree for connectivity query [java](java/structures/LinkCutTreeConnectivity.java) [**kotlin**](kotlin/structures/LinkCutTreeConnectivity.kt)
+ [x] Link/cut tree for LCA query [java](java/structures/LinkCutTreeLca.java) [**kotlin**](kotlin/structures/LinkCutTreeLca.kt)
+ [x] Binary heap [java](java/structures/BinaryHeap.java) [**kotlin**](kotlin/structures/BinaryHeap.kt)
+ [x] Binary heap with change priority [c++](cpp/structures/binary_heap.cpp) [java](java/structures/BinaryHeapExtended.java) [**kotlin**](kotlin/structures/BinaryHeapExtended.kt)
+ [x] Disjoint sets [c++](cpp/structures/disjoint_sets.cpp) [java](java/structures/DisjointSets.java) [**kotlin**](kotlin/structures/DisjointSets.kt)
+ [x] Treap [c++](cpp/structures/treap.h) [java](java/structures/Treap.java) [kotlin](kotlin/Treap.kt) [**kotlin**](kotlin/structures/Treap.kt)
+ [x] Treap with indexed key [c++](cpp/structures/treap_indexed.cpp) [java](java/structures/TreapIndexed.java) [**kotlin**](kotlin/structures/TreapIndexed.kt)
+ [x] k-d tree for point query [c++](cpp/structures/kd_tree.cpp) [java](java/structures/KdTreePointQuery.java) [**kotlin**](kotlin/structures/KdTreePointQuery.kt)
+ [x] k-d tree for rectangular query [java](java/structures/KdTreeRectQuery.java) [**kotlin**](kotlin/structures/KdTreeRectQuery.kt)
+ [x] R-tree [java](java/structures/RTree.java) [**kotlin**](kotlin/structures/RTree.kt)
+ [x] Metric tree [java](java/structures/MetricTree.java) [**kotlin**](kotlin/structures/MetricTree.kt)
+ [x] Quadtree [java](java/structures/QuadTree.java) [**kotlin**](kotlin/structures/QuadTree.kt)
+ [x] Mergeable heap [java](java/structures/MergeableHeap.java) [**kotlin**](kotlin/structures/MergeableHeap.kt)
+ [x] Queue with minimum [c++](cpp/structures/queue_min.cpp) [java](java/structures/QueueMin.java) [**kotlin**](kotlin/structures/QueueMin.kt)
+ [x] Sparse table [c++](cpp/structures/sparse-table.cpp) [java](java/structures/RmqSparseTable.java) [java](java/graphs/lca/LcaSparseTable.java) [**kotlin**](kotlin/structures/RmqSparseTable.kt)
+ [x] Sparse segment tree [**c++**](cpp/structures/sparse-segment-tree.cpp)
+ [x] Wavelet tree [c++](cpp/structures/wavelet_tree.cpp) [java](java/structures/WaveletTree.java) [**kotlin**](kotlin/structures/WaveletTree.kt)
+ [x] Mo's algorithm [java](java/structures/MosAlgorithm.java) [**kotlin**](kotlin/structures/MosAlgorithm.kt)
+ [x] Mo's algorithm with point updates [**c++**](cpp/structures/mos_with_updates.cpp)

#### Graph algorithms
+ [x] Shortest paths [**c++**](cpp/graphs/shortestpaths) [**java**](java/graphs/shortestpaths)
+ [x] Maximum flow [**c++**](cpp/graphs/flows) [**java**](java/graphs/flows)
+ [x] Maximum matching [**c++**](cpp/graphs/matchings) [**java**](java/graphs/matchings)
+ [x] Spanning tree [**c++**](cpp/graphs/spanningtree) [**java**](java/graphs/spanningtree)
+ [x] Connectivity [**c++**](cpp/graphs/dfs) [**java**](java/graphs/dfs)
+ [x] Biconnectivity [java](java/graphs/dfs/Biconnectivity.java) [**kotlin**](kotlin/graphs/dfs/Biconnectivity.kt)
+ [x] LCA Schieber-Vishkin algorithm [c++](cpp/graphs/lca/lca_rmq_schieber_vishkin.cpp) [java](java/graphs/lca/LcaSchieberVishkin.java) [**kotlin**](kotlin/graphs/lca/LcaSchieberVishkin.kt)
+ [x] LCA [**java**](java/graphs/lca)
+ [ ] Planarity testing ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/28))
+ [ ] Dynamic graph connectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/29))
+ [ ] Chu–Liu/Edmonds' algorithm ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/30))
+ [ ] Minimum augmentation to strong connectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/32))
+ [ ] Minimum augmentation to biconnectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/33))

#### String algorithms
+ [x] Knuth-Morris-Pratt algorithm [c++](cpp/strings/kmp.cpp) [java](java/strings/Kmp.java) [**kotlin**](kotlin/strings/Kmp.kt)
+ [x] Aho-Corasick algorithm [c++](cpp/strings/aho-corasick.cpp) [java](java/strings/AhoCorasick.java) [**kotlin**](kotlin/strings/AhoCorasick.kt)
+ [x] Suffix array and lcp array. Radix sort algorithm in O(n*log(n)) [c++](cpp/strings/suffix-array.cpp) [java](java/strings/SuffixArray.java) [**kotlin**](kotlin/strings/SuffixArray.kt)
+ [x] Suffix array. Algorithm DC3 in O(n) [c++](cpp/strings/suffix-array-dc3.cpp) [java](java/strings/SuffixArrayDC3.java) [**kotlin**](kotlin/strings/SuffixArrayDC3.kt)
+ [x] Suffix array. Algorithm SA-IS in O(n) [**c++**](cpp/strings/suffix-array-sa-is.cpp)
+ [x] Suffix automaton [c++](cpp/strings/suffix-automaton.cpp) [java](java/strings/SuffixAutomaton.java) [**kotlin**](kotlin/strings/SuffixAutomaton.kt)
+ [x] Suffix tree Ukkonen's algorithm [c++](cpp/strings/suffix_tree_ukkonen.cpp) [java](java/strings/SuffixTree.java) [**kotlin**](kotlin/strings/SuffixTree.kt)
+ [x] Suffix tree Breslauer-Italiano algorithm [**c++**](cpp/strings/suffix_tree_breslauer_italiano.cpp)
+ [x] Trie [java](java/strings/Trie.java) [**kotlin**](kotlin/strings/Trie.kt)
+ [x] Z-function [c++](cpp/strings/z-function.cpp) [java](java/strings/ZFunction.java) [**kotlin**](kotlin/strings/ZFunction.kt)
+ [x] Hashing [c++](cpp/strings/hashing.cpp) [java](java/strings/Hashing.java) [**kotlin**](kotlin/strings/Hashing.kt)
+ [x] Parsing [**java**](java/parsing) [**c++**](cpp/parsing)
+ [ ] Palindrome tree ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/34))
+ [ ] Sorting strings in linear time ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/31))

#### Sorting algorithms
+ [x] Sorting algorithms [c++](cpp/sort/sort.cpp) [java](java/sort/Sort.java) [**kotlin**](kotlin/sort/Sort.kt)
+ [x] N-th element [java](java/sort/NthElement.java) [**kotlin**](kotlin/sort/NthElement.kt)

#### Geometry algorithms
+ [x] Segments intersection [c++](cpp/geometry/segments_intersection.cpp) [java](java/geometry/SegmentsIntersection.java) [**kotlin**](kotlin/geometry/SegmentsIntersection.kt)
+ [x] Line operations [java](java/geometry/LineGeometry.java) [**kotlin**](kotlin/geometry/LineGeometry.kt)
+ [x] Circle operations [java](java/geometry/CircleOperations.java) [**kotlin**](kotlin/geometry/CircleOperations.kt)
+ [x] Convex hull [c++](cpp/geometry/convex_hull.cpp) [java](java/geometry/ConvexHull.java) [**kotlin**](kotlin/geometry/ConvexHull.kt)
+ [x] Point in polygon query [c++](cpp/geometry/point_in_polygon.cpp) [java](java/geometry/PointInPolygon.java) [**kotlin**](kotlin/geometry/PointInPolygon.kt)
+ [x] Closest pair of points [java](java/geometry/Closest2Points.java) [**kotlin**](kotlin/geometry/Closest2Points.kt)
+ [x] Furthest pair of points [**c++**](cpp/geometry/diameter.cpp)
+ [ ] Implement quaternion ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/35))

#### Optimization
+ [x] Simplex algorithm [java](java/optimization/Simplex.java) [**kotlin**](kotlin/optimization/Simplex.kt)

#### Numerical algorithms
+ [x] Fast Fourier transform (FFT) [c++](cpp/numeric/fft.h) [java](java/numeric/FFT.java) [**kotlin**](kotlin/numeric/FFT.kt)
+ [x] Long arithmetics [**c++**](cpp/numeric/bigint.cpp)
+ [x] Fast subset convolution [java](java/numeric/SubsetConvolution.java) [**kotlin**](kotlin/numeric/SubsetConvolution.kt)
+ [x] Fast Walsh-Hadamar transform [java](java/numeric/WalshHadamarTransform.java) [**kotlin**](kotlin/numeric/WalshHadamarTransform.kt)
+ [x] Karatsuba multiplication [java](java/numeric/KaratsubaMultiply.java) [**kotlin**](kotlin/numeric/KaratsubaMultiply.kt)
+ [x] Newton interpolation [java](java/numeric/NewtonInterpolation.java) [**kotlin**](kotlin/numeric/NewtonInterpolation.kt)
+ [x] Laguerre's root-finding algorithm [**c++**](cpp/numeric/polynom-roots.cpp)

#### Number theory
+ [x] Primes and divisors [java](java/numbertheory/PrimesAndDivisors.java) [c++](cpp/numbertheory/primes_and_divisors.cpp) [**kotlin**](kotlin/numbertheory/PrimesAndDivisors.kt)
+ [x] Factorization [java](java/numbertheory/Factorization.java) [c++](cpp/numbertheory/factorization.cpp) [**kotlin**](kotlin/numbertheory/Factorization.kt)
+ [x] Euclidean algorithm [java](java/numbertheory/Euclid.java) [c++](cpp/numbertheory/euclid.cpp) [**kotlin**](kotlin/numbertheory/Euclid.kt)
+ [x] Primitive root [**c++**](cpp/numbertheory/primitive_root.cpp)
+ [x] Discrete logarithm [**c++**](cpp/numbertheory/discrete_log.cpp)
+ [x] Discrete root [**c++**](cpp/numbertheory/discrete_root.cpp)
+ [x] Multiplicative function [java](java/numbertheory/MultiplicativeFunction.java) [**kotlin**](kotlin/numbertheory/MultiplicativeFunction.kt)
+ [x] Rational numbers [java](java/numbertheory/Rational.java) [**kotlin**](kotlin/numbertheory/Rational.kt)
+ [x] Polynom class [**c++**](cpp/numbertheory/polynom.h)
+ [x] Linear recurrence and Berlekamp-Massey algorithm [**c++**](cpp/numbertheory/linear_recurrence.cpp)
+ [x] Modular operations [**c++**](cpp/numbertheory/modint.h)

#### Combinatorics
+ [x] Permutations [java](java/combinatorics/Permutations.java) [**kotlin**](kotlin/combinatorics/Permutations.kt)
+ [x] Combinations [java](java/combinatorics/Combinations.java) [**kotlin**](kotlin/combinatorics/Combinations.kt)
+ [x] Arrangements [java](java/combinatorics/Arrangements.java) [**kotlin**](kotlin/combinatorics/Arrangements.kt)
+ [x] Partitions [java](java/combinatorics/Partitions.java) [**kotlin**](kotlin/combinatorics/Partitions.kt)
+ [x] Set Partitions [java](java/combinatorics/SetPartitions.java) [**kotlin**](kotlin/combinatorics/SetPartitions.kt)
+ [x] Bracket sequences [java](java/combinatorics/BracketSequences.java) [**kotlin**](kotlin/combinatorics/BracketSequences.kt)
+ [x] Binomial coefficients [java](java/combinatorics/BinomialCoefficients.java) [**kotlin**](kotlin/combinatorics/BinomialCoefficients.kt)
+ [x] Prufer code [java](java/combinatorics/PruferCode.java) [**kotlin**](kotlin/combinatorics/PruferCode.kt)

#### Linear algebra
+ [x] Gaussian elimination [c++](cpp/linearalgebra/gauss.cpp) [java](java/linearalgebra/Gauss.java) [kotlin](kotlin/Gauss.kt) [**kotlin**](kotlin/linearalgebra/Gauss.kt)
+ [x] Determinant calculation [java](java/linearalgebra/Determinant.java) [**kotlin**](kotlin/linearalgebra/Determinant.kt)
+ [x] Matrix operations [c++](cpp/linearalgebra/matrix.h) [java](java/linearalgebra/Matrix.java) [**kotlin**](kotlin/linearalgebra/Matrix.kt)


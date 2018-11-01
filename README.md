# ssgodbol_hw2

Run Instructions:
1. Clone the Repo
2. Go to the folder "ssgodbol_hw2"
3. Run ```make```
4. Run the tests


Assumptions:
1. When we add a node it creates a new ring of itself. When we join two nodes they form a ring with them and pre-existing nodes in their ring.
2. It follows eventual consistency so it may need some number of stabilize (less than number of nodes in ring) before it gives consistent output.

Algorithm:
Referring to algorithm as explained in [this](https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf) paper (given in the homework)

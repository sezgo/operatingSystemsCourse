Task:	SiO2
-   In the process of manufacturing EPROM memory, a layer of silicon dioxide (SiO2) is needed.
-   In order to form the silicon layer, you need to have two atoms of oxygen and one atom of silicon present at the
same time.
-   Write a program which, using semaphores, will help in the process of EPROM memory production. 
-   Each of the atoms represents a seperate process.
-   The silicon atoms (processes) execute the proc_si() method, while the oxygen atoms execute the proc_o() method.
-   After all three atoms "meet", each of them calls the virtual method bond(), to form the SIO2 molecule.

Limitations:
-   If a silicon atom reaches the "barrier" first, he will have to wait for the two oxygen atoms to arrive.
-   If an oxygen atom arrives first, he will wait for a silicon atom and another oxygen atom.
-   Only one silicon and two oxygen atoms can leave the barrier at the same time - this is the point which we can
 consider a silicon dioxide (SIO2) molecule is formed.
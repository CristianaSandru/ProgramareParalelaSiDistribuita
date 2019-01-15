// MPIHelloWorld.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "mpi.h"
#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <cassert>
#include <chrono>
using namespace std;
ifstream fin("vector.txt");
ofstream fout("vector.txt");

void putInFile(int num_elements)
{
	for (int i = 0; i < num_elements + 1; i++) {
		fout << 2 << " ";
	}
	fout.close();
}
void readFromFile(float *rand_nums, int num_elements)
{
	int nr;
	for (int i = 0; i < num_elements; i++) {
		fin >> nr;
		rand_nums[i] = nr;
	}
}

int main(int argc, char* argv[])
{
	if (argc != 2) {
		fprintf(stderr, "Usage: avg num_elements_per_proc\n");
		exit(1);
	}

	int num_elements = atoi(argv[1]);

	float *rand_nums = (float *)malloc(sizeof(float) * num_elements);

	putInFile(num_elements);
	readFromFile(rand_nums, num_elements);

	chrono::high_resolution_clock::time_point start = chrono::high_resolution_clock::now();
	MPI_Init(NULL, NULL);

	int world_rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
	int world_size;
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);

	int nr_elem_proces = num_elements / world_size;
	int inceput = nr_elem_proces * world_rank;
	int sfarsit = inceput + nr_elem_proces;

	if (world_rank == world_size - 1) //las process add rest of numbers
		sfarsit = num_elements;

	// Sum the numbers locally
	float local_sum = 0;
	int i;
	for (i = inceput; i < sfarsit; i++) {
		local_sum += rand_nums[i];
	}

	// Print the random numbers on each process
	cout << "Local sum for process with number " << world_rank << " is " << local_sum << endl;

	//--// Reduce all of the local sums into the global sum
	float global_sum;
	MPI_Reduce(&local_sum, &global_sum, 1, MPI_FLOAT, MPI_SUM, 0, MPI_COMM_WORLD);

	// Print the result
	if (world_rank == 0) {
		chrono::high_resolution_clock::time_point end = chrono::high_resolution_clock::now();
		float elapsedTime = chrono::duration_cast<chrono::milliseconds>(end - start).count();
		cout << "Total sum " << global_sum << " || " << " Timp " << elapsedTime << endl;
	}

	// Clean up
	free(rand_nums);

	MPI_Barrier(MPI_COMM_WORLD);
	MPI_Finalize();

	int b;
	cin >> b;
}

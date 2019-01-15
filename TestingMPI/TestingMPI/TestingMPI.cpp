#include "pch.h"
#include <iostream>
#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <cassert>
#include <chrono>
#include <fstream>
#include <cstdlib>

using namespace std;

ifstream f_input("input.txt"); // punctele
ifstream f_polinom("polinom.txt"); // coeficientii
ofstream o_input("input.txt"); // punctele
ofstream o_polinom("polinom.txt"); // coeficientii
ofstream f_output("output.txt"); // result

void populateCoeficienti(int num_elements)
{
	for (int i = 0; i < num_elements; i++) {
		o_polinom << (rand() % 1000) << " ";
	}
	o_polinom.close();
}

void readCoeficienti(int *data, int num_elements)
{
	int nr;
	for (int i = 0; i < num_elements; i++) {
		f_polinom >> nr;
		data[i] = nr;
	}
	f_polinom.close();
}

void populateValues(int xsize)
{
	for (int i = 0; i < xsize; i++) {
		o_input << (rand() % 1000) << " ";
	}
	o_input.close();
}

int main(int argc, char* argv[])
{
	chrono::high_resolution_clock::time_point start_all = chrono::high_resolution_clock::now();
	int x;
	int x_size = 10000;
	int *data;
	int result = 1;
	chrono::high_resolution_clock::time_point start = chrono::high_resolution_clock::now();
	MPI_Init(NULL, NULL);

	int rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	int size;
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int num_elements = size;

	data = new int[num_elements];

	int number;
	// prepare the data
	if (rank == 0) {
		populateCoeficienti(num_elements);
		readCoeficienti(data, num_elements);
		populateValues(x_size);
	}

	// Sending value to each process
	MPI_Scatter(data, 1, MPI_INT, &number, 1, MPI_INT, 0, MPI_COMM_WORLD);

	MPI_Barrier(MPI_COMM_WORLD);
	std::cout << "Procesul: " << rank << " a primit valoarea: " << number << std::endl;

	for (int i = 0; i < x_size; i++) {
		f_input >> x;

		MPI_Bcast(&x, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Status status;
		int MPI_TAG = 1;
		int source = rank + 1;
		int destination = rank - 1;

		if (source < size) {
			MPI_Recv(&result, 1, MPI_INT, source, MPI_TAG, MPI_COMM_WORLD, &status);
		}

		if (destination >= 0) {
			result = number + result * x;
			MPI_Send(&result, 1, MPI_INT, destination, MPI_TAG, MPI_COMM_WORLD);
		}
		else {
			f_output << result << " ";
		}

		MPI_Barrier(MPI_COMM_WORLD);
	}
	f_input.close();
	free(data);

	MPI_Barrier(MPI_COMM_WORLD);

	chrono::high_resolution_clock::time_point end_all = chrono::high_resolution_clock::now();
	float elapsedTime_all = chrono::duration_cast<chrono::milliseconds>(end_all - start_all).count();
	cout << "Pentru procesul: " << rank << ", Timp total: " << elapsedTime_all << " milliseconds." << endl;
	//f_output << "Pentru procesul: " << rank << ", Timp total: " << elapsedTime_all << " milliseconds." << "\n";

	MPI_Finalize();

	return 0;
}
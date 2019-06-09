#include <stdio.h>
#include <stdlib.h> 
#include <signal.h> // sigaction(), sigsuspend(), sig*()
#include <unistd.h> // alarm()
#include <string.h>

int main(int argc, char const *argv[])
{
    int my_pid, oponent_pid;
    FILE *inFile; 
    char buffer1[128] ; 
    inFile = fopen ("test.txt", "r");
    fgets( buffer1, 128, inFile );
    oponent_pid = atoi (buffer1);
    printf (" done. \noponent_pid %d \n", oponent_pid);
    fclose(inFile);
    
    
    return 0;
}

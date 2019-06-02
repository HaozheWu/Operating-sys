#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>


/**
 * pipe stdout to stdin and read from stdin  
 *
 */

int main(int argc, char **argv)
{

int pipefd[2];
pipe(pipefd); 

char *args[] = {"tee", NULL};


if (fork() ==0 ){

close(pipefd[0]);
dup2(pipefd[1],1); 
execvp("tee", args ) ; 

}else{

close(pipefd[1]); 
//char buffer[1024];

char *buffer = (char *)malloc(1024);
char **ptr = &buffer;
size_t bufsize = 1024;
int rdSize;
int count= 0 ; 

while (rdSize = getline(&buffer, &bufsize, stdin) > 0)
{
printf("%d %s", count,buffer);
count +=1 ; 
}

// while (read(pipefd[0], buffer, sizeof(buffer)) != 0)
// {
// printf("buffer %s", buffer); 
// }



}







}

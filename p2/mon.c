#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>


/* the program execution starts here */
int main(int argc, char **argv)
{
    char    *program;
    int pid1, pid2, pid3;
    int cnt = 0;
    int status;
 
    pid1 = fork();
    if (pid1 < 0){
        exit(-1);
    }else if (pid1 == 0){
        
        execlp("./calcloop.exe","calcloop",NULL); 
    }

    pid2 = fork();
    if (pid2 < 0){
    	exit(-1);
    }else if(pid2 == 0){

	
	//char buf [10]; 
        sprintf(program , "%d", pid1 );
	//const char* p = buf; 
    	execlp("./procmon.exe","procmon", program , NULL);

    }

    wait(&status); 
    wait(&status); 

     

    
    pid3 = fork();
    
    if (pid3<0){
   	exit(-1);
    }else if (pid3 ==0 ){
    	execlp("./cploop.exe","cploop",NULL);
    }

    pid2 = fork();
    if (pid2 <0) {
   	 exit(-1);
    }else if (pid2 == 0){
    	sprintf(program, "%d", pid3 );
    	execlp("./procmon.exe","procmon",program, NULL); 
    }

    wait(&status) ; 
    wait(&status); 







	//wait(&status) ; printf("pid %d ended", pid1);

   /*	1. launch the program 'calcloop' and get its pid
        2. fork/launch 'procmon pid' where pid is the pid of the process launched in step 1
        3. wait till calcloop process ends
        4. wait till procmon process ends
        5. fork/launch the program 'cploop' and get its pid
        6. fork/launch 'procmon pid' where pid is the pid of the process launched in step 5
        7. wait till cploop process ends
        8. wait till procmon process ends
    */
}

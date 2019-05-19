/*--------------------------------------------------------------------------
File: cploop.c

Description:  This program first creates a file containing 500,000 x characters
              in the file "fromfile.txt" and then runs 10 iterations of a loop that
		1) sleeps for 3 seconds
		2) copies the file "fromfile@ to file "tofile.txt"
--------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#define BFSIZE 1
int main()
{
   int i;
   FILE *frfp;
   FILE *tofp;
   int frf;
   int tof;
   int sz;
   char buffer[BFSIZE];

    /* create the file */
   frfp = fopen("fromfile.txt","w");
   if(frfp==NULL)exit(1);
   for(i=0 ; i<500000 ; i++) fputc('x',frfp);
   fclose(frfp);

    /* the main loop */
   for(i=0; i<10 ; i++)  /* only loop 10 times */
   {
       sleep(3);
           /* open the files */
       frf = open("fromfile.txt",O_RDONLY); /* for reading */
       tof = open("tofile.txt",O_WRONLY|O_CREAT); /* for writing */
         /* copy bytes one at a time */
       while( (sz=read(frf,buffer,BFSIZE)) != 0) write(tof,buffer,sz);
         /* close files */
       close(frf);
       close(tof);
   }
   remove("fromfile.txt");
   remove("tofile.txt");
}

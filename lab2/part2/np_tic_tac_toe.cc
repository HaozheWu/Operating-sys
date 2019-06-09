
#include <stdio.h>
#include <stdlib.h> 
#include <signal.h> // sigaction(), sigsuspend(), sig*()
#include <unistd.h> // alarm()
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include "tic_tac_toe.h"


/* Usage example
 * 
 * First, compile and run this program:
 *     $ gcc np_tic_tac_toe.c -o np_tic_tac_toe
 *     if not already created, create a named pipe (FIFO)
 *     $ mkfifo my_pipe 
 *
 *     In two separe shells run:
 *     $ ./np_tic_tac_toe X 
 *     $ ./np_tic_tac_toe O 
 * 
 *
 */
 
 
int main(int argc, char **argv) {
  char player;
  tic_tac_toe *game = new tic_tac_toe();

  int fd;

  char myfifo[128] = "my_pipe";
  int turn = 0 ; 
  
  
  if (argc != 2) {
    printf ("Usage: sig_tic_tac_toe [X|O] \n");
    return (-1);
  }
  player = argv[1][0];
  if (player != 'X' && player != 'O') {
    printf ("Usage: player names must be either X or Y");
    return (-2);
  }

  if (player == 'X'){
    turn = 1 ; 
    sleep(3);
  }
  

  while (true){

    if (turn&1 ){
      char str[128] ;
      char * ptr; 
      fd = open(myfifo, O_WRONLY);
      game->get_player_move(player);
      printf("\n");
      game->display_game_board();
      ptr = game->convert2string(); 

      int index = 0 ; 
      while(*ptr != '\0'){
        str[index] = *ptr; 
        ptr++; 
        index +=1 ; 
      }
      write(fd, str, strlen(str)+1);
      close(fd);
      if(game->game_result() != '-'){
        break; 

      }else {
        turn ++ ; 
      }


    }else {
      printf("Opponent's move:\n");
      char str[128] ;
      char * ptr; 
      fd = open(myfifo, O_RDONLY);
      read(fd, str, 128);
      game->set_game_state(str); 
      game->display_game_board(); 
      close(fd) ; 
      if(game->game_result() != '-'){
        break; 

      }else {

        turn ++ ; 
      }

    }

  }

   printf ("Game finished, result: %c \n", game->game_result());

  // do {

  //   if (turn & 1 ){
  //     char str[128] ;
  //     char * ptr; 
      
  //     fd = open(myfifo, O_WRONLY);
  //     game->get_player_move(player);
  //     ptr = game->convert2string(); 

  //     int index = 0 ; 
  //     while(*ptr != '\0'){
  //       str[index] = *ptr; 
  //       ptr++; 
  //       index +=1 ; 
  //     }
      
  //     write(fd, str, strlen(str)+1);
  //     close(fd);
  //     fd = open(myfifo, O_RDONLY);
  //     read(fd, str, 128);
  //     game->set_game_state(str); 
  //     game->display_game_board(); 
  //     close(fd) ; 
  //     turn++;
      

  //   }else{
  //     char str[128] ;
  //     char * ptr; 
  //     fd = open(myfifo, O_RDONLY);
  //     read(fd, str, 128);
  //     game->set_game_state(str); 
  //     game->display_game_board(); 
  //     close(fd) ; 
  //     fd = open(myfifo, O_WRONLY);
  //     game->get_player_move(player);
  //     ptr = game->convert2string(); 

  //     int index = 0 ; 
  //     while(*ptr != '\0'){
  //       str[index] = *ptr; 
  //       ptr++; 
  //       index +=1 ; 
  //     }
      
  //     write(fd, str, strlen(str)+1);
  //     close(fd);

  //     turn++;

  //   }

  // } while ((player = game->game_result()) == '-');



  return (0);
}

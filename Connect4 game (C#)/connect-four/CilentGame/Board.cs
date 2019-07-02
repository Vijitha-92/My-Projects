using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CilentGame

{
    class Board
    {
        enum Color { Free, Olive, Brown };
        private Color[,] mResult;
        private bool player1, gameended;
        public void Mark(int row, int column)
        {
            if (row >= 0)
            {
                if (mResult[row, column] == Color.Free)
                {
                    mResult[row, column] = player1 ? Color.Olive : Color.Brown;
                    player1 ^= true;
                }
            }
        }
        public Board()
        {

            mResult = new Color[6, 7];
            player1 = true;
            gameended = false;

        }

        public int NetPosition(int column)
        {
            for (int y = 5; y >= 0; y--)
                if (mResult[y, column] == 0)
                    return y;
            return -1;

        }
        public bool Winner()
        {
            for (int r = 0; r <= 5; r++)
            {
                for (int c = 0; c <= 3; c++)
                {
                    if (mResult[r, c] != 0)
                    {
                        if ((mResult[r, c] == mResult[r, c + 1]) && (mResult[r, c] == mResult[r, c + 2]) && (mResult[r, c] == mResult[r, c + 3]))
                        {

                            return true;
                        }

                    }

                }
            }
            for (int r = 0; r <= 2; r++)
            {
                for (int c = 0; c <= 6; c++)
                {
                    if (mResult[r, c] != 0)
                    {
                        if ((mResult[r, c] == mResult[r + 1, c]) && (mResult[r, c] == mResult[r + 2, c]) && (mResult[r, c] == mResult[r + 3, c]))
                        {

                            return true;
                        }



                    }

                }

            }
            for (int c = 0; c <= 3; c++)
            {
                for (int r = 0; r <= 2; r++)
                {
                    if (mResult[r, c] != 0)
                    {
                        if ((mResult[r, c] == mResult[r + 1, c + 1]) && (mResult[r, c] == mResult[r + 2, c + 2]) && (mResult[r, c] == mResult[r + 3, c + 3]))
                        {
                            return true;
                        }
                    }
                }
                for (int r = 3; r <= 5; r++)
                {
                    if (mResult[r, c] != 0)
                    {
                        if ((mResult[r, c] == mResult[r - 1, c + 1]) && (mResult[r, c] == mResult[r - 2, c + 2]) && (mResult[r, c] == mResult[r - 3, c + 3]))
                        {

                            return true;
                        }

                    }

                }

            }
            return false;

        }
    }
}


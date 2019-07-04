using System;
using System.Collections.Generic;
using System.IO;
using System.Xml.Serialization;

namespace Game11._2
{
    public enum Color { Free, Olive, Brown };

    /// <summary>
    /// It's a board  which marks the players and check for winner
    /// </summary>
   public class Board
    {
        public Color[,] mResult;
        //  private Color[,] saveBoard;
        public List<Color> savedlist = new List<Color>(6 * 7);
        private bool gameended;

        /// <summary>
        /// mark with the respective player in a particular column,row
        /// </summary>
        /// <param name="row">
        /// row value
        /// </param>
        /// <param name="column">
        /// column value
        /// </param>
        /// <param name="player1">
        /// true for player1
        /// </param>
        /// 
       
        public void Mark(int row, int column, bool player1)
        {
            if (row >= 0)
            {
                if (mResult[row, column] == Color.Free)
                {
                    mResult[row, column] = player1 ? Color.Olive : Color.Brown;

                }
            }
        }
        public Board()
        {

            mResult = new Color[6, 7];

            gameended = false;

        }
        /// <summary>
        /// save the mark board in to a file
        /// </summary>
        /// <param name="val">
        /// true for save otherwise false
        /// </param>


        public bool Save(bool val)
        {

            if (val)
            {
                for (int i = 0; i < 6; i++)
                {
                    for (int j = 0; j < 7; j++)
                    {
                        savedlist.Add(mResult[i, j]);
                    }
                }
                Stream stream = File.OpenWrite(Environment.CurrentDirectory + "\\mytext.txt");
                XmlSerializer serial = new XmlSerializer(typeof(List<Color>));
                serial.Serialize(stream, savedlist);
                stream.Close();
                return true;
            }
            return false;
        }

        /// <summary>
        /// check for down free row in that particular column
        /// </summary>
        /// <param name="column">
        /// column value
        /// </param>
        /// <returns>
        /// row value when it's free ortherwise -1
        /// </returns>

        public int NetPosition(int column)
        {
            for (int y = 5; y >= 0; y--)
                if (mResult[y, column] == 0)
                    return y;
            return -1;

        }
        /// <summary>
        /// checks for win or block with three in row,three in column,three in diagonally
        /// </summary>
        /// <param name="col">
        /// column value
        /// </param>
        /// <returns>
        /// return col value when it's free otherwise -1
        /// </returns>

        public int WinorBlock(Color col)
        {

            for (int c = 0; c <= 3; c++)
            {
                int r = NetPosition(c);
                if (r >= 0 && r < 6 && r != -1)
                {
                    if ((mResult[r, c] == 0) && (mResult[r, c + 1] == (mResult[r, c + 2])) && (mResult[r, c + 3] == mResult[r, c + 2]) && (mResult[r, c + 1] == col))
                    {
                        return c;
                    }

                    if ((mResult[r, c + 1] == Color.Free) && (mResult[r, c] == mResult[r, c + 2]) && (mResult[r, c + 2] == mResult[r, c + 3]) && (mResult[r, c] == col))
                    {

                        return c + 1;
                    }
                    if ((mResult[r, c + 2] == Color.Free) && (mResult[r, c] == mResult[r, c + 1]) && (mResult[r, c + 3] == mResult[r, c]) && (mResult[r, c] == col))
                    {
                        return c + 2;
                    }
                    if ((mResult[r, c + 3] == Color.Free) && (mResult[r, c] == mResult[r, c + 1]) && (mResult[r, c] == mResult[r, c + 2]) && (mResult[r, c] == col))
                    {
                        return c + 3;
                    }
                }

            }
            for (int c = 0; c <= 6; c++)
            {
                int r = NetPosition(c);
                //
                if (r < 3 && r != -1)
                {
                    if ((mResult[r, c] == Color.Free) && (mResult[r + 1, c] == mResult[r + 2, c]) && (mResult[r + 3, c] == mResult[r + 1, c]) && (mResult[r + 1, c] == col))
                    {

                        return c;
                    }

                    if ((mResult[r + 1, c] == Color.Free) && (mResult[r, c] == mResult[r + 2, c]) && (mResult[r + 3, c] == mResult[r, c]) && (mResult[r, c] == col))
                    {

                        return c + 1;
                    }
                    if ((mResult[r + 2, c] == Color.Free) && (mResult[r + 1, c] == mResult[r, c]) && (mResult[r, c] == mResult[r + 3, c]) && (mResult[r, c] == col))
                    {

                        return c + 2;
                    }
                    if ((mResult[r + 3, c] == Color.Free) && (mResult[r + 1, c] == mResult[r + 2, c]) && (mResult[r, c] == mResult[r + 1, c]) && (mResult[r, c] == col))
                    {

                        return c + 3;
                    }

                }

            }
            for (int c = 0; c <= 3; c++)
            {
                int r = NetPosition(c);
                if (r <= 2 && r != -1)
                {
                    if ((mResult[r, c] == 0) && (mResult[r + 1, c + 1] == mResult[r + 2, c + 2]) && (mResult[r + 1, c + 1] == mResult[r + 3, c + 3]) && (mResult[r + 1, c + 1] == col))
                    {
                        return c;
                    }
                    if ((mResult[r + 1, c + 1] == 0) && (mResult[r, c] == mResult[r + 2, c + 2]) && (mResult[r, c] == mResult[r + 3, c + 3]) && (mResult[r, c] == col))
                    {
                        return c + 1;
                    }
                    if ((mResult[r + 2, c + 2] == 0) && (mResult[r + 1, c + 1] == mResult[r, c]) && (mResult[r + 1, c + 1] == mResult[r + 3, c + 3]) && (mResult[r + 1, c + 1] == col))
                    {
                        return c + 2;
                    }
                    if ((mResult[r + 3, c + 3] == 0) && (mResult[r + 1, c + 1] == mResult[r, c]) && (mResult[r + 1, c + 1] == mResult[r + 2, c + 2]) && (mResult[r + 1, c + 1] == col))
                    {
                        return c + 3;
                    }


                }
                if (r >= 3 && r <= 5 && r != -1)
                {
                    if ((mResult[r, c] == 0) && (mResult[r - 1, c + 1] == mResult[r - 2, c + 2]) && (mResult[r - 1, c + 1] == mResult[r - 3, c + 3]) && (mResult[r - 1, c + 1] == col))
                    {
                        return c;

                    }
                    if ((mResult[r - 1, c + 1] == 0) && (mResult[r, c] == mResult[r - 2, c + 2]) && (mResult[r, c] == mResult[r - 3, c + 3]) && (mResult[r, c] == col))
                    {
                        return c + 1;

                    }
                    if ((mResult[r - 2, c + 2] == 0) && (mResult[r - 1, c + 1] == mResult[r, c]) && (mResult[r - 1, c + 1] == mResult[r - 3, c + 3]) && (mResult[r - 1, c + 1] == col))
                    {
                        return c + 2;

                    }
                    if ((mResult[r - 3, c + 3] == 0) && (mResult[r - 1, c + 1] == mResult[r - 2, c + 2]) && (mResult[r - 1, c + 1] == mResult[r, c]) && (mResult[r - 1, c + 1] == col))
                    {
                        return c + 3;

                    }
                }
            }

            return -1;
        }
        /// <summary>
        /// checks for win with four in row,four in column,four in diagonally

        /// <returns>
        /// returns true if winner found otherwise false
        /// </returns>


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

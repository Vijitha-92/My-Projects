using System;
using System.Collections.Generic;
using System.Windows.Documents;
using Game11._2;

using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UnitBoardTest
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void NetPostionTest()
        {
            //Arrange
            Board board = new Board();
            //Act
            int result = board.NetPosition(4);
            //Assert
            Assert.AreEqual(5, result);

        }
        [TestMethod]
        public void NetPostionNegTest()
        {
            //Arrange
            Board board = new Board();
            board.mResult[5, 0] = board.mResult[3, 0] = board.mResult[1, 0] = Color.Brown;
            board.mResult[4, 0] = board.mResult[2, 0] = board.mResult[0, 0] = Color.Olive;
            //Act
            int result = board.NetPosition(0);
            //Assert
            Assert.AreEqual(-1, result);

        }
        [TestMethod]
        public void WinorBlockTest()
        {
            //Arrange
            Color color = Color.Brown;
            Board board = new Board();
            board.mResult[2, 0] = Color.Free;
            board.mResult[4, 0] = board.mResult[3, 0] = board.mResult[5, 0] = color;

            //Act
            int result = board.WinorBlock(color);
            Console.WriteLine(result);

            //Assert
            Assert.AreEqual(0, result);

        }
        [TestMethod]
        public void WinorBlockNegativeTest()
        {
            //Arrange
            Color color = Color.Brown;
            Board board = new Board();
            board.mResult[2, 0] = Color.Olive;
            board.mResult[4, 0] = board.mResult[3, 0] = board.mResult[5, 0] = color;

            //Act
            int result = board.WinorBlock(color);
            //Assert
            Assert.AreEqual(-1, result);

        }
        [TestMethod]
        public void WinnerNegativeTest()
        {
            //Arrange
            Color color = Color.Brown;
            Board board = new Board();
            board.mResult[2, 0] = Color.Olive;
            board.mResult[4, 0] = board.mResult[3, 0] = board.mResult[5, 0] = color;
            //Act
            bool result = board.Winner();
            //Assert
            Assert.AreEqual(false, result);

        }
        [TestMethod]
        public void WinnerTest()
        {
            //Arrange
            Color color = Color.Brown;
            Board board = new Board();
            board.mResult[4, 0] = board.mResult[3, 0] = board.mResult[5, 0] = board.mResult[2, 0] = color;
            //Act
            bool result = board.Winner();
            //Assert
            Assert.AreEqual(true, result);
        }
        [TestMethod]
        public void MarkTest()
        {
            //Arrange
            Color result;
            Board board = new Board();
            //Act
            board.Mark(4, 4, true);
            //Assert
            result = board.mResult[4, 4];
            Assert.AreEqual(Color.Olive, result);

        }
        [TestMethod]
        public void MarkNegativeTest()
        {
            //Arrange
            Color result;
            Board board = new Board();
            //Act
            board.Mark(4, 4, true);
            //Assert
            result = board.mResult[0, 4];
            Assert.AreEqual(Color.Free, result);

        }
        [TestMethod]
        public void MarkPlayrer2Test()
        {
            //Arrange
            Color result;
            Board board = new Board();
            //Act
            board.Mark(4, 4, false);
            //Assert
            result = board.mResult[4, 4];
            Assert.AreEqual(Color.Brown, result);

        }
        [TestMethod]
        public void SaveNegativeTest()
        {
            //Arrange
            List<Color> savedlist = new List<Color>(6 * 7);
            Board board = new Board();
            //Act
            bool result = board.Save(false);
            //Assert

            Assert.AreEqual(false, result);

        }
        [TestMethod]
        public void SaveTest()
        {
            //Arrange
            List<Color> savedlist = new List<Color>(6 * 7);
            Board board = new Board();
            //Act
            bool result = board.Save(true);
            //Assert

            Assert.AreEqual(true, result);

        }

    }
       
}

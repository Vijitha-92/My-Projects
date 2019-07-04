
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;
using System.Xml.Serialization;

namespace Game11._2
{
    public partial class MainWindow : Window
    {
        private Board board = new Board();
        private bool gameEnded = false;
        private bool loop = true;
        private bool player1 = true, save;
        private TcpListener listener;
        private TcpClient cilentSocket = new TcpClient();
        private NetworkStream serverStream = default(NetworkStream);
        private StreamReader reader;
        private IPAddress addressIp;
        private string readData = null;
        public bool Is_Network { get; set; }
        public bool Is_AI { get; set; }
        public StreamWriter Writer { get; set; }
        public event EventHandler<int> newColumnRecieved;
        public MainWindow()
        {
            InitializeComponent();
            newColumnRecieved += MainWindow_newColumnRecieved;
        }
        /// <summary>
        /// Drop the coin
        /// </summary>
        /// <param name="column">
        /// column value</param>
        private void DropCoin(int column)
        {
            if (gameEnded)
            {
                Board board = new Board();
                return;
            }
            int row = board.NetPosition(column);
            board.Mark(row, column, player1);
            Draw_Circle(row, column);
            if (board.Winner())
            {
                gameEnded = true;
                if (player1)
                    MessageBox.Show("Olive  is the Winner ");
                else
                    MessageBox.Show("Maroon  is the Winner ");
            }
            player1 ^= true;
        }
        /// <summary>
        /// Invoking the main thread to drop coin from network thread
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MainWindow_newColumnRecieved(object sender, int e)
        {
            Application.Current.Dispatcher.Invoke((Action<int>)DropCoin, e);

        }
        /// <summary>
        /// Gets column no form ellipse mouse button
        /// </summary>
        /// <param name="sender">ellipse</param>
        /// <param name="e"> button</param>
        private void Ellipse_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            var eclipse = (Ellipse)sender;
            var column = Grid.GetColumn(eclipse);
            if (Is_AI)
            {
                Against_Computer(column);
            }
            if (Is_Network)
            {
                Against_Network(column);
            }
        }
        /// <summary>
        /// Against AI
        /// </summary>
        /// <param name="column">column value</param>
        private void Against_Computer(int column)
        {
            if (player1)
                DropCoin(column);
            else
            {
                int win = board.WinorBlock(Color.Brown);
                int block = board.WinorBlock(Color.Olive);
                if (win != -1)
                {
                    DropCoin(win);
                }
                else if (block != -1)
                {
                    DropCoin(block);
                }
                else
                {
                    Random rnd = new Random();
                    int num = rnd.Next(7);
                    int row = board.NetPosition(num);
                    if (row > 0)
                    DropCoin(num);
                }
            }
        }
        /// <summary>
        /// Against Network
        /// </summary>
        /// <param name="column"></param>
        private void Against_Network(int column)
        {
            if (player1)
            {
                string data = column.ToString();
                Writer.WriteLine(data);
                Writer.Flush();
                DropCoin(column);
            }
        }
        /// <summary>
        /// Starts the network
        /// </summary>
        private void Startbtn_Click(object sender, RoutedEventArgs e)
        {
            string input = iptext.Text;
            bool validInput = IPAddress.TryParse(input, out addressIp);
            if (validInput)
            {
                Start_Network(addressIp);
            }
            else
                MessageBox.Show("Invalid IPaddress");
        }
        /// <summary>
        /// Accept the cilent connection 
        /// </summary>
        /// <param name="address">Ip address</param>
        private void Start_Network(IPAddress address)
        {
            listener = new TcpListener(address, 8080);
            listener.Start();
            Console.WriteLine("listener started");
            cilentSocket = listener.AcceptTcpClient();
            Is_Network = true;
            Writer = new StreamWriter(cilentSocket.GetStream());
            Thread serverThread = new Thread(new ThreadStart(GetMessage));
            serverThread.Start();
        }
        /// <summary>
        /// Gets the message from cilent
        /// </summary>
        private void GetMessage()
        {
            if (cilentSocket.Connected)
            {
                while (loop)
                {
                    serverStream = cilentSocket.GetStream();
                    reader = new StreamReader(serverStream);
                    readData = reader.ReadLine();
                    int col = int.Parse(readData);
                    newColumnRecieved?.Invoke(this, col);
                }
            }
            else
            {
                if (loop == true) loop = false;
                if (cilentSocket != null)
                {
                    cilentSocket.Close();
                }
            }
        }
        /// <summary>
        /// Stops Network
        /// </summary>
        private void Stopbtn_Click(object sender, RoutedEventArgs e)
        {
            if (listener != null)
            {
                listener.Stop();
            }
        }
        /// <summary>
        /// Set in AI mode
        /// </summary>
        private void AI_Click(object sender, RoutedEventArgs e)
        {
            Is_AI = true;
        }
        /// <summary>
        /// Loads the game
        /// </summary>
        private void Load_Click(object sender, RoutedEventArgs e)
        {
            Stream stream = File.OpenRead(Environment.CurrentDirectory + "\\mytext.txt");
            try
            {
                XmlSerializer serial = new XmlSerializer(typeof(List<Color>));
                List<Color> input = (List<Color>)serial.Deserialize(stream);
                for (int i = 0; i < 6; i++)
                {
                    for (int j = 0; j < 7; j++)
                    {
                        if (input[7 * i + j].ToString().Equals("Olive"))
                        {
                            player1 = true;
                            DropCoin(j);
                        }
                        else if (input[7 * i + j].ToString().Equals("Brown"))
                        {
                            player1 = false;
                            DropCoin(j);
                        }
                        else
                        {
                            return;
                        }
                    }
                }
                stream.Close();
                File.Delete(Environment.CurrentDirectory + "\\mytext.txt");
            }
            catch (Exception)
            {
                MessageBox.Show("error");
            }
        }
        /// <summary>
        /// Save the game in Xml
        /// </summary>
        private void Save_Click(object sender, RoutedEventArgs e)
        {
            board.Save(true);
            Close();
        }
        /// <summary>
        /// Draws the circle in the particular row and column
        /// </summary>
        /// <param name="row"> row</param>
        /// <param name="column">column</param>
        private void Draw_Circle(int row, int column)
        {
            Ellipse circle = new Ellipse();
            circle.Name = "Ellipse1";
            circle.StrokeThickness = 5;
            circle.Stroke = Brushes.Black;
            circle.Width = 84;
            circle.Height = 84;
            circle.Fill = player1 ? Brushes.Olive : Brushes.Brown;
            Grid.SetRow(circle, row);
            Grid.SetColumn(circle, column);
            root.Children.Add(circle);
        }
    }
}


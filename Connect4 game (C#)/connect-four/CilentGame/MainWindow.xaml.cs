using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;


namespace CilentGame
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public event EventHandler<int> newColumnRecieved;
        Board board = new Board();
        private bool network;

        public bool Network { get => network; set => network = value; }
        private bool gameended = false;
        private bool loop = true;
        private bool player1 = true;
        private TcpClient cilentSocket = new TcpClient();
        private NetworkStream serverStream = default(NetworkStream);
        StreamWriter writer1;
        string readdata = null;
        StreamReader reader;


        public MainWindow()
        {
            InitializeComponent();
            newColumnRecieved += MainWindow_newColumnRecieved;
        }

        private void MainWindow_newColumnRecieved(object sender, int e)
        {
            Application.Current.Dispatcher.Invoke((Action<int>)DropCoin, e);
            //DropCoin(e);
        }

        private void DropCoin(int column)
        {
            if (gameended)
            {
                Board board = new Board();
                return;
            }

            int row = board.NetPosition(column);
            board.Mark(row, column);

            Ellipse childCtrl = new Ellipse();
            childCtrl.Name = "Ellipse1";
            childCtrl.StrokeThickness = 5;
            childCtrl.Stroke = Brushes.Black;
            childCtrl.Width = 84;
            childCtrl.Height = 84;
            childCtrl.Fill = player1 ? Brushes.Olive : Brushes.Brown;
            Grid.SetRow(childCtrl, row);
            Grid.SetColumn(childCtrl, column);
            root.Children.Add(childCtrl);
            if (board.Winner())
            {
                gameended = true;
                if (player1)
                    MessageBox.Show("OLIVE  is the Winner ");
                else
                    MessageBox.Show("BROWN is the Winner ");
            }

            player1 ^= true;

        }

        private void Ellipse_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            var eclipse = (Ellipse)sender;
            var column = Grid.GetColumn(eclipse);
            if (network)
            {
                if (!player1)
                {
                    String data = column.ToString();
                    writer1.WriteLine(data);
                    writer1.Flush();
                    DropCoin(column);
                }
            }
        }
        private void connectbtn_Click(object sender, RoutedEventArgs e)
        {
            cilentSocket.Connect("127.0.0.1", 8080);
            Console.WriteLine("Cilent connected");
            network = true;
            Thread serverThread = new Thread(new ThreadStart(getMessage));
            serverThread.Start();
            writer1 = new StreamWriter(cilentSocket.GetStream());

        }

        private void getMessage()
        {

            //string returndata;
            while (loop)
            {

                serverStream = cilentSocket.GetStream();
                reader = new StreamReader(serverStream);

                readdata = reader.ReadLine();
                int col = int.Parse(readdata);

                newColumnRecieved?.Invoke(this, col);
            }
        }
    }
}

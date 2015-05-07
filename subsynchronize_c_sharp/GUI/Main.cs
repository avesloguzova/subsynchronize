using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using SubsynchronizeLib;

namespace GUI
{
    public partial class Main : Form
    {
        public Main()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            DialogResult result = openFileDialog1.ShowDialog();
            if (result == DialogResult.OK) 
            {
                string file = openFileDialog1.FileName;
                textBox2.Text = file;
            }
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            int subtitleDurationInSec = 6;
            
            var streamChunker = new StreamChunker(textBox2.Text, subtitleDurationInSec);
            var byteChunks = streamChunker.GetStreams();

            StreamRecogniser recogniser = new StreamRecogniser();

            List<String> texts = await Task.Run(() => byteChunks.Select(chunk => recogniser.Recognise(chunk)).ToList());

            List<Subtitle> subtitles = await Task.Run(() => Subtitle.GetSubtitles(texts, subtitleDurationInSec));

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < subtitles.Count; i++)
            {
                var subtitle = subtitles[i];
                sb.Append((i + 1) + Environment.NewLine + subtitle + Environment.NewLine);
            }

            textBox1.Text = sb.ToString();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            saveFileDialog1.FilterIndex = 2;
            saveFileDialog1.RestoreDirectory = true;
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                File.WriteAllText(saveFileDialog1.FileName, textBox1.Text);
                MessageBox.Show("Save!");
            }
        }
    }
}

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NAudio.Wave;

namespace voice_recognition_c_sharp
{
    class Program
    {
        static void Main(string[] args)
        {
            int subtitleDurationInSec = 3;
            string outputFile = @"C:\Users\Denis\Desktop\voice_recognition_c_sharp\voice_recognition_c_sharp\voice_recognition_c_sharp\text.txt";

            var streamChunker = new StreamChunker("D:\\FakeDesktop\\ESLPod11001.wav", subtitleDurationInSec);
            var byteChunks = streamChunker.GetStreams();

            StreamRecogniser recogniser = new StreamRecogniser();

            List<String> texts = byteChunks.Select(chunk => recogniser.Recognise(chunk)).ToList();

            List<Subtitle> subtitles = Subtitle.GetSubtitles(texts, subtitleDurationInSec);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < subtitles.Count; i++)
            {
                var subtitle = subtitles[i];
                sb.Append((i + 1) + Environment.NewLine + subtitle + Environment.NewLine);
            }

            File.WriteAllText(outputFile, sb.ToString());

            Console.ReadKey();
        }
    }
}

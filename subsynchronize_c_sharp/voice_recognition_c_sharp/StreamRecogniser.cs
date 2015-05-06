using System;
using System.IO;
using System.Speech.Recognition;
using System.Text;

namespace voice_recognition_c_sharp
{
    /// <summary>
    /// Получаем текст из стрима
    /// </summary>
    class StreamRecogniser
    {
        private SpeechRecognitionEngine sre;
        private readonly Grammar grammar;

        public StreamRecogniser()
        {
            grammar = new DictationGrammar();
        }

        /// <summary>
        /// Распознать Поток в строку
        /// </summary>
        public string Recognise(byte[] bytes)
        {
            sre = new SpeechRecognitionEngine();
            sre.LoadGrammar(grammar);
            sre.BabbleTimeout = new TimeSpan(Int32.MaxValue);
            sre.InitialSilenceTimeout = new TimeSpan(Int32.MaxValue);
            sre.EndSilenceTimeout = new TimeSpan(100000000);
            sre.EndSilenceTimeoutAmbiguous = new TimeSpan(100000000);
            var stream = new MemoryStream(bytes);
            sre.SetInputToWaveStream(stream);

            StringBuilder sb = new StringBuilder();
            while (true)
            {
                try
                {
                    var recText = sre.Recognize();
                    if (recText == null)
                    {
                        break;
                    }

                    sb.Append(recText.Text + " ");
                }
                catch (Exception ex)
                {
                    break;
                }
            }
            
            sre.UnloadAllGrammars();
            return sb.ToString();
        }
    }
}

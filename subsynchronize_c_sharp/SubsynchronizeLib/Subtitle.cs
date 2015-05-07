using System;
using System.Collections.Generic;

namespace SubsynchronizeLib
{
    /// <summary>
    /// Субтитры
    /// </summary>
    public class Subtitle
    {
        public string Text { get; set; }
        public TimeSpan Start { get; set; }
        public TimeSpan Stop { get; set; }


        /// <summary>
        /// Получить список субтитров
        /// </summary>
        /// <param name="texts">Фразы для сабтитров</param>
        /// <param name="subtitleDuration">Время воспроизведения одного субтитра</param>
        /// <returns></returns>
        public static List<Subtitle> GetSubtitles(List<String> texts, int subtitleDuration)
        {
            List<Subtitle> subtitles = new List<Subtitle>();
            var startTime = new TimeSpan(0, 0, 0, 0);

            foreach (string text in texts)
            {
                var t = new TimeSpan(0, 0, 0, subtitleDuration);

                var subtitle = new Subtitle
                {
                    Text = text,
                    Start = startTime,
                    Stop = startTime.Add(t)
                };

                subtitles.Add(subtitle);

                startTime = subtitle.Stop;
            }

            return subtitles;
        }

        public override string ToString()
        {
            return Start.ToString() + " --> " + Stop.ToString() + Environment.NewLine + Text + Environment.NewLine;
        }
    }
}
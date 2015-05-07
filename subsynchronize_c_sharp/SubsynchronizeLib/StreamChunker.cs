using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using NAudio.Wave;

namespace SubsynchronizeLib
{
    /// <summary>
    /// Разделяем поток на части, эквивалентные длине одного субтитра
    /// </summary>
    public class StreamChunker
    {
        private readonly string waveFilePath;
        private readonly int lengthOfOneSubtitle;
        private WavHeader header;
        private readonly byte[] data;

        struct WavHeader
        {
            public byte[] riffID;
            public uint size;
            public byte[] wavID;
            public byte[] fmtID;
            public uint fmtSize;
            public ushort format;
            public ushort channels;
            public uint sampleRate;
            public uint bytePerSec;
            public ushort blockSize;
            public ushort bit;
            public byte[] dataID;
            public uint dataSize;
        }


        public StreamChunker(string waveFilePath, int lengthOfOneSubtitle)
        {
            this.waveFilePath = waveFilePath;
            this.lengthOfOneSubtitle = lengthOfOneSubtitle;
            byte[] bytes = File.ReadAllBytes(waveFilePath);
            MemoryStream stream = new MemoryStream(bytes);

            header = new WavHeader();

            using (BinaryReader br = new BinaryReader(stream))
            {
                try
                {
                    header.riffID = br.ReadBytes(4);
                    header.size = br.ReadUInt32();
                    header.wavID = br.ReadBytes(4);
                    header.fmtID = br.ReadBytes(4);
                    header.fmtSize = br.ReadUInt32();
                    header.format = br.ReadUInt16();
                    header.channels = br.ReadUInt16();
                    header.sampleRate = br.ReadUInt32();
                    header.bytePerSec = br.ReadUInt32();
                    header.blockSize = br.ReadUInt16();
                    header.bit = br.ReadUInt16();
                    header.dataID = br.ReadBytes(4);
                    header.dataSize = br.ReadUInt32();

                    data = br.ReadBytes((int)header.dataSize);
                }
                finally
                {
                    br.Close();
                    stream.Close();
                }
            }
        }

        /// <summary>
        /// Возвращает набор стримов, каждый из которых представляет собой отдельный субтитр
        /// </summary>
        public List<byte[]> GetStreams()
        {
            var chunkSize = header.bytePerSec * lengthOfOneSubtitle;

            var byteChunks = ArraySplit(data, (int)chunkSize).ToList();

            List<byte[]> result = new List<byte[]>();

            using (MemoryStream stream = new MemoryStream())
            {
                using (BinaryWriter bw = new BinaryWriter(stream))
                {
                    bw.Write(header.riffID);
                    bw.Write(header.size);
                    bw.Write(header.wavID);
                    bw.Write(header.fmtID);
                    bw.Write(header.fmtSize);
                    bw.Write(header.format);
                    bw.Write(header.channels);
                    bw.Write(header.sampleRate);
                    bw.Write(header.bytePerSec);
                    bw.Write(header.blockSize);
                    bw.Write(header.bit);
                    bw.Write(header.dataID);
                    bw.Write(header.dataSize);
                }
                stream.Flush();
                //                int size = System.Runtime.InteropServices.Marshal.SizeOf(typeof(WavHeader));

                byte[] bytes = stream.GetBuffer();

                byte[] b = new byte[72];
                Array.Copy(bytes, 0, b, 0, 72);

                foreach (byte[] t in byteChunks)
                {
                    var z = new byte[b.Length + t.Length];
                    b.CopyTo(z, 0);
                    t.CopyTo(z, b.Length);
                    result.Add(z);
                }
            }

            return result;
        }

        private IEnumerable<byte[]> ArraySplit(byte[] bArray, int intBufforLengt)
        {
            int bArrayLenght = bArray.Length;
            byte[] bReturn = null;

            int i = 0;
            for (; bArrayLenght > (i + 1) * intBufforLengt; i++)
            {
                bReturn = new byte[intBufforLengt];
                Array.Copy(bArray, i * intBufforLengt, bReturn, 0, intBufforLengt);
                yield return bReturn;
            }

            int intBufforLeft = bArrayLenght - i * intBufforLengt;
            if (intBufforLeft > 0)
            {
                bReturn = new byte[intBufforLeft];
                Array.Copy(bArray, i * intBufforLengt, bReturn, 0, intBufforLeft);
                yield return bReturn;
            }
        }
    }
}


using PCClient;
using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows.Forms;

public static class ADBManage
{
    public static bool ADBStart(MainWindow mw)
    {
        bool result = false;
        Process p = new Process();
        p.StartInfo.FileName = "adb.exe";
        p.StartInfo.Arguments = "forward tcp:8000 tcp:9000";
        p.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;

        try
        {
            p.Start();
            p.WaitForExit();

            if (p.ExitCode != 0)
            {
                mw.Label_Status.Content = "안드로이드가 Debugging 모드가 아니거나 여러대가 연결되어 있습니다.\r\n확인 후 프로그램을 재시작하세요.";
            }
            else
                result = true;
        }
        catch(Win32Exception we)
        {
            mw.Label_Status.Content = "adb를 찾을 수 없습니다.\r\n같은 폴더에 설치 후 프로그램을 재시작하세요.";
        }
        catch(Exception e)
        {
            MessageBox.Show("ADBStart Error\n" + e.ToString());
        }

        return result;
    }

    public static void ADBEnd()
    {
        Process p = new Process();
        p.StartInfo.FileName = "adb.exe";
        p.StartInfo.Arguments = "forward --remove tcp:8000";
        p.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;

        try
        {
            p.Start();
        }
        catch (Exception e)
        {
            MessageBox.Show("ADBEnd Error\n" + e.ToString());
        }
    }
}
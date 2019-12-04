import UIKit
import app

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        
//        let model = GoModel()
//
//        model.forIOS()
        
        let db = DbModel(dbId: 123)
        
        db.showDbStuff()
        
//        let startTime = CFAbsoluteTimeGetCurrent()
//
//        let model = GoModel()
//        for n in 0...100000 {
//            model.addData(key: "arst\(n)", data: SomeData(s: "tnnnt\(n)"))
//        }
//        let timeElapsed = CFAbsoluteTimeGetCurrent() - startTime
//        print("added \(model.dataCount) time: \(timeElapsed)")
//
//        for n in 0...100000 {
//            model.getData(key: "arst\(n)")
//        }
//
//        let timeElapsed2 = CFAbsoluteTimeGetCurrent() - startTime
//        print("read \(model.dataCount) time: \(timeElapsed2)")
//
//        model.doStuffBlocking()
//        model.doStuff(block: showString)
//        label.text = Proxy().proxyHello()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    func showString(s:String) -> Void {
        label.text = s
    }
    @IBOutlet weak var label: UILabel!
}


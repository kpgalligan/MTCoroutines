import UIKit
import app

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let model = GoModel()
        model.doStuffBlocking()
        model.doStuff(block: showString)
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


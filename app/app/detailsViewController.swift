//
//  detailsViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/15/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

import UIKit

class detailsViewController: UIViewController, UITableViewDelegate,UIPickerViewDataSource,UIPickerViewDelegate {
    
    
    @IBOutlet weak var scroller: UIScrollView!
    @IBOutlet weak var detailsLabel: UITextView!
    @IBOutlet weak var subTypeLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subTypePicker: UIPickerView!
 
    var pickerDataArray: [String] = []
    override func viewDidLoad() {

        scroller.scrollEnabled = true
        scroller.contentSize = CGSize(width:0
, height:900)
        subTypeLabel.text=""
      //  adMob.loadBanners(self)
        var data : NSDictionary = general.MyVariables.contestData["details"] as NSDictionary
        print(general.MyVariables.contestData["name"] as String)
        setTitle(general.MyVariables.contestData["name"] as String)
        setDetails(data)
        
        general.contest.subtypesArray = general.MyVariables.contestData["subtype"] as NSArray
        for subtype in general.contest.subtypesArray{
            let subtype: String = subtype["distance"] as String
            pickerDataArray.append(subtype)
        }
        subTypePicker.dataSource = self
        subTypePicker.delegate = self
        
        //subTypePicker.selectRow(0, inComponent: 0, animated: true)
        pickerView(subTypePicker, didSelectRow: 0, inComponent: 0)
            
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerDataArray.count
    }
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String! {
        return pickerDataArray[row]
    }
    
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        subTypeLabel.textAlignment = NSTextAlignment.Right;
        let distance = (general.contest.subtypesArray[row]["distance"] as String)
        let price = (general.contest.subtypesArray[row]["price_normal"] as String)
        let late_price = (general.contest.subtypesArray[row]["price_late"] as String)
    //    let type = (general.contest.subtypesArray[row]["type"] as String)
    //    let level = (general.contest.subtypesArray[row]["dificulty_level"] as String)
        
        var subTypeText =  "מחיר הרשמה: " + price + " ש׳׳ח "
        subTypeText+="\n"
        subTypeText+="מחיר הרשמה מאוחרת: " + late_price + " ש׳׳ח "
        subTypeLabel.text = subTypeText
        general.contest.link = (general.contest.subtypesArray[row]["link"] as String)
    }
    
    func setTitle(title : String){
        titleLabel.text = title
        titleLabel.textAlignment = NSTextAlignment.Center;
    }
    
    func setDetails(details : NSDictionary){
        
        let desc1:String = details["row1"] as String
    
        let date:String=general.MyVariables.contestData["date"] as String
        var detailsStr:String = desc1
        detailsStr+="\n"
        detailsLabel.text = detailsStr
        detailsLabel.textAlignment = NSTextAlignment.Right;
        detailsLabel.scrollRangeToVisible(NSMakeRange(0, 1))
        detailsLabel.flashScrollIndicators()
    }
    @IBAction func regButtonClick(sender: AnyObject) {
        if(general.isConnectedToNetwork()){
            adMob.createAndLoadInterstitial()
            adMob.displayInterstitial(self)
        }

    }
}
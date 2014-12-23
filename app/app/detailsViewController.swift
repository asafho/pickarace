//
//  detailsViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/15/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

import UIKit

class detailsViewController: UIViewController, UITableViewDelegate {
 
    @IBOutlet weak var detailsLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        var data : NSDictionary = general.MyVariables.contestData["details"] as NSDictionary
        setTitle(general.MyVariables.contestData["name"] as String)
        setDetails(data)
        println(data["details"])
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    func setTitle(title : String){
        titleLabel.text = title
        titleLabel.textAlignment = NSTextAlignment.Center;
    }
    
    func setDetails(details : NSDictionary){
        var detailsStr : String = ""
        detailsStr+="פרטי האירוע:\n"
        detailsStr=detailsStr+"מקלחות יש, זה אין"
        println(detailsStr)
        detailsLabel.text = detailsStr
        detailsLabel.textAlignment = NSTextAlignment.Right;
    }
    
}
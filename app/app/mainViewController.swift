//
//  ViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/10/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

import UIKit
import iAd

class mainViewController: UIViewController, GADBannerViewDelegate, ADBannerViewDelegate {
    
    
    override func viewDidLoad() {
        if(general.isConnectedToNetwork()){
            adMob.ads.interstitial = adMob.createAndLoadInterstitial()
        }
        super.viewDidLoad()
      
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func runningBtn(sender: AnyObject) {
        println("Running button clicked")
        adMob.displayInterstitial(self)
        general.MyVariables.contest="running"
        changeView()
        
    }

    @IBAction func swimmingBtn(sender: AnyObject) {
        println("Swimming button clicked")
        general.MyVariables.contest="swimming"
        changeView()
    }

    func changeView(){
        let view = self.storyboard?.instantiateViewControllerWithIdentifier("listView") as listViewController
        self.navigationController?.pushViewController(view, animated: true)
    }



}


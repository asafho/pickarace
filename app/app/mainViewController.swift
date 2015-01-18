//
//  ViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/10/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

import UIKit
import iAd

class mainViewController: UIViewController, GADBannerViewDelegate, GADInterstitialDelegate, ADBannerViewDelegate {

    override func viewDidLoad() {
        adMob.loadBanners(self)
        general.setContestsFromURL()
        super.viewDidLoad()
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func runningBtn(sender: AnyObject) {
        println("Running button clicked")
        general.MyVariables.contest="running"
        changeView()
        
    }

    @IBAction func swimmingBtn(sender: AnyObject) {
        println("Swimming button clicked")
        general.MyVariables.contest="swimming"
        changeView()
    }

    @IBAction func triathlonBtn(sender: AnyObject) {
        println("triathlon button clicked")
        general.MyVariables.contest="triathlon"
        changeView()
    }
    
    @IBAction func bikingBtn(sender: AnyObject) {
        println("Biking button clicked")
        general.MyVariables.contest="biking"
        changeView()
    }
    
    func changeView(){
        adMob.displayInterstitial(self)
        let view = self.storyboard?.instantiateViewControllerWithIdentifier("listView") as listViewController
        self.navigationController?.pushViewController(view, animated: true)
        loadAd()
    }
    
    func loadAd(){
        if(general.isConnectedToNetwork()){
            adMob.ads.interstitial = adMob.createAndLoadInterstitial()
        }
    }



}


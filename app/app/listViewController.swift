//
//  listViewController.swift
//  app
//
//  Created by Asaf Horovitz on 12/10/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//


import UIKit

class listViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    let cellIdentifier: String = "contestsCell"

    @IBOutlet var contestsTableView : UITableView?
    var tableData = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getContests()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getContests() {
        let urlPath = "https://s3-us-west-2.amazonaws.com/com.cuefit.data/contests.json"
        let url = NSURL(string: urlPath)
        let session = NSURLSession.sharedSession()
        let task = session.dataTaskWithURL(url, completionHandler: {data, response, error -> Void in
        println("Task completed")
        if(error != nil) {
            // If there is an error in the web request, print it to the console
            println(error.localizedDescription)
        }
        var err: NSError?
        var jsonResult = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: &err) as NSDictionary
        if(err != nil) {
            // If there is an error parsing JSON, print it to the console
            println("JSON Error \(err!.localizedDescription)")
        }
        let results: NSArray = jsonResult["events"] as NSArray
        for contest in results{
            let contestValue: String = contest["type"] as String
            if(contestValue != general.MyVariables.contest){
            //  println(contest["name"])
              println(results.indexOfObject(contest))
              (results as NSMutableArray).removeObject(contest)
            }
        }
         
           
        dispatch_async(dispatch_get_main_queue(), {
            self.tableData = results
            self.contestsTableView!.reloadData()
            })
        })
        task.resume()
    }
      

    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell: UITableViewCell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier) as UITableViewCell
        let rowData: NSDictionary = self.tableData[indexPath.row] as NSDictionary
        
        let eventType: String = rowData["type"] as String
        if(eventType==general.MyVariables.contest){
            cell.textLabel?.text = rowData["name"] as? String
            let id: String = rowData["id"] as String
            let vendor: String = rowData["vendor"] as String
            let date: String = rowData["date"] as String
            var desc = "Date:"+date+" ,Vendor: "+vendor
            cell.detailTextLabel?.text = desc
        
   /*   
            let imgURL: NSURL? = NSURL(string: urlString)
            // Download an NSData representation of the image at the URL
        let imgData = NSData(contentsOfURL: imgURL!)
        cell.imageView.image = UIImage(data: imgData!)
     */
        // Get the formatted price string for display in the subtitle
       
        }
        else{
            
        }
        return cell

    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        // Get the row data for the selected row
 
        let view = self.storyboard?.instantiateViewControllerWithIdentifier("detailsView") as detailsViewController
        self.navigationController?.pushViewController(view, animated: true)
        general.MyVariables.contestData = self.tableData[indexPath.row] as NSDictionary
        
        /*    var name: String = rowData["name"] as String
        var date: String = rowData["date"] as String
        var details: NSDictionary = rowData["details"] as NSDictionary
        var alert: UIAlertView = UIAlertView()
        alert.title = name
        alert.message = date
        alert.addButtonWithTitle("Ok")
        alert.show()
*/
    }
}
